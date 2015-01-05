package com.blossom.muc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Element;
import org.jivesoftware.openfire.PacketException;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.jivesoftware.openfire.muc.MUCRole.Role;
import org.jivesoftware.openfire.muc.spi.LocalMUCUser;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.util.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.Presence;

import com.blossom.test.component.Constants;
import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupMessage;
import com.blossom.test.entity.GroupPriMessage;
import com.blossom.test.entity.GroupSysMessage;
import com.blossom.test.util.PacketUtil;

public class LocalGroupUser extends LocalMUCUser {

	private static final Logger Log = LoggerFactory.getLogger(LocalGroupUser.class);
	protected Map<Long, LocalGroupRole> roles = new ConcurrentHashMap<Long, LocalGroupRole>();
	
	private GroupServiceImpl server;
	private GroupDbManager dbManager;
	private PacketUtil packetUtil;
	
	public LocalGroupUser(GroupServiceImpl chatservice, PacketRouter packetRouter, JID jid) {
		super(chatservice, packetRouter, jid);
		this.server = chatservice;
		packetUtil = PacketUtil.getInstance();
		dbManager = GroupDbManager.getInstance();
	}

	public void process(Packet packet) throws UnauthorizedException,
			PacketException {
		if (packet instanceof IQ) {
			process((IQ) packet);
		} else if (packet instanceof Message) {
			process((Message) packet);
		} else if (packet instanceof Presence) {
			process((Presence) packet);
		}
	}
	
	public void process(IQ packet) {
		// Ignore IQs of type ERROR or RESULT sent to a room
        if (IQ.Type.error == packet.getType()) {
            return;
        }
        lastPacketTime = System.currentTimeMillis();
        JID recipient = packet.getTo();
        long group_id = -1;
        try {
        	group_id = Long.parseLong(recipient.getNode());
		} catch (Exception e) {
			sendErrorPacket(packet, PacketError.Condition.bad_request);
		}
        LocalGroupRole role = roles.get(group_id);
    	if (role == null) {
            sendErrorPacket(packet, PacketError.Condition.not_authorized);
        } else if (IQ.Type.result == packet.getType()) {
            // Only process IQ result packet if it's a private packet sent to another
            // room occupant
            if (packet.getTo().getResource() != null) {
                try {
                    // User is sending an IQ result packet to another room occupant
                    role.getChatRoom().sendPrivatePacket(packet, role);
                }
                catch (NotFoundException e) {
                    // Do nothing. No error will be sent to the sender of the IQ result packet
                }
            }
        } else {
            // Check and reject conflicting packets with conflicting roles
            // In other words, another user already has this nickname
            if (!role.getUserAddress().equals(packet.getFrom())) {
                sendErrorPacket(packet, PacketError.Condition.conflict);
            }
            else {
                try {
                    Element query = packet.getElement().element("query");
                    if (query != null && "http://jabber.org/protocol/muc#admin".equals(query.getNamespaceURI())) {
                        IQ reply = ((LocalGroup)role.getChatRoom()).getIqAdminHandler().handleIQ(packet);
                        router.route(reply);
                    } else {
                        if (packet.getTo().getResource() != null) {
                            // User is sending an IQ packet to another room occupant
                            role.getChatRoom().sendPrivatePacket(packet, role);
                        } else {
                            sendErrorPacket(packet, PacketError.Condition.bad_request);
                        }
                    }
				} catch (NotFoundException e) {
					sendErrorPacket(packet, PacketError.Condition.recipient_unavailable);
				} catch (Exception e) {
					sendErrorPacket(packet, PacketError.Condition.internal_server_error);
					Log.error(e.getMessage(), e);
				}
            }
        }
	}
	
	public void process(Presence packet) {
        // Ignore presences of type ERROR sent to a room
        if (Presence.Type.error == packet.getType()) {
            return;
        }
        lastPacketTime = System.currentTimeMillis();
        Element mucInfo = packet.getChildElement("x", "http://jabber.org/protocol/muc");
        if (mucInfo != null) {
            try {
       			Long.parseLong(packet.getTo().getNode());
            	sendGroupHisMessage(packet);
            //新建群，群名称不能为纯数字
    		} catch (NumberFormatException e) {
    			LocalGroup localGroup = server.getGroup(packet);
    			Group group = localGroup.getGroup();
    			packetUtil.sendPacket(packetUtil.createGroupCreatePresence(group, packet.getFrom().toBareJID()), ComponentManagerFactory.getComponentManager());
    		}
        }
	}
	
	public void process(Message message) {
		Type type = message.getType();
		//群聊
		if (type == Type.groupchat) {
			//改群名称
			if (message.getElement().element("subject") != null)
				renameGroup(message);
			//邀请用户加入
			else if (message.getChildElement("x", "http://jabber.org/protocol/muc#user") != null)
				inviteMember(message);
			//发送群聊信息
			else if (message.getBody() != null)
				sendGroupMessage(message);
		//私聊
		} else if (type == Type.chat) {
			//群成员私聊
			if (message.getElement().element("group") != null)
				sendPrivateMessage(message);
		}
	}
	
	public void addGroupRole(long group_id, LocalGroupRole role) {
		roles.put(group_id, role);
	}
	
	/**
	 * 请求历史消息
	 * 
	 * @param presence
	 */
	private void sendGroupHisMessage(Presence presence) {
		//当前时间
		long time = System.currentTimeMillis();
		String member_jid = presence.getFrom().toBareJID();
		JID to = presence.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			sendErrorPacket(presence, PacketError.Condition.bad_request);
		}
		//删除群成员
		try {
			LocalGroup group = server.getGroup(group_id);
			if (group == null) {
				sendErrorPacket(presence, PacketError.Condition.bad_request);
			} else if (group.isMember(presence.getFrom().toBareJID())) {
				//取请求条件
				Element history = presence.getChildElement("x", "http://jabber.org/protocol/muc").element("history");
				Map<String, String> params = new HashMap<String, String>();
				putMapNotEmpty(params, history, "maxstanzas");
				putMapNotEmpty(params, history, "seconds");
				putMapNotEmpty(params, history, "since");
				putMapNotEmpty(params, history, "maxchars");
				List<GroupMessage> list = GroupDbManager.getInstance().listGroupHisMessage(group_id, params, time);
				if (list != null && !list.isEmpty()) {
					
					for (GroupMessage message : list) {
						Message msg = packetUtil.createHisMessage(message, member_jid);
						router.route(msg);
					}
				}
			}
		} catch (SQLException e) {
		}
	}
	
	/**
	 * 重命名群
	 * 
	 * @param message
	 */
	private void renameGroup(Message message) {
		String from = message.getFrom().toBareJID();
		JID to = message.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		LocalGroup group = server.getGroup(group_id);
		String new_name = message.getElement().element("subject").getText();
		//新名字不为空
		if (new_name != null && !"".equals(new_name.trim())) {
			try {
				//检查是否有权限
				if (group != null && group.isOwner(from)) {
					//改名
					dbManager.renameGroup(group_id, new_name);
					group.getGroup().setGroup_name(new_name);
					//通知群成员
					Message notice_msg = packetUtil.createRenameMessage(group_id, new_name, from);
					sendPacket(group.getGroupMembers().keySet(), notice_msg);
				}
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * 邀请成员加入
	 * 
	 * @param message
	 */
	private void inviteMember(Message message) {
		Element x = message.getChildElement("x", "http://jabber.org/protocol/muc#user");
		Element invite = x.element("invite");
		Element decline = x.element("decline");
		//from
		String from = message.getFrom().toBareJID();
		JID to = message.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		LocalGroup localGroup = server.getGroup(group_id);
		if (localGroup != null) {
			//群主邀请用户
			if (invite != null && invite.attribute("to") != null) {
				String invitee = invite.attributeValue("to");
				if (invitee != null && !"".equals(invitee.trim())) {
					try {
						//取群信息
//						Group group = dbManager.getGroupById(group_id);
						if (localGroup.isOwner(from) 
								&& !localGroup.isMember(new JID(invitee).toBareJID())) {
							String reason = invite.elementText("reason");
							//生成系统消息
							Message msg = packetUtil.createInviteMessage(localGroup.getGroup(), from, invitee, reason);
							sendSysMessage(msg, new JID(invitee));
//						if (!sendPacketIfOnline(msg, invitee)) {
//							GroupSysMessage sysMessage = new GroupSysMessage(invitee, msg.toXML());
//							dbManager.insertGroupSysMessage(sysMessage);
//						}
						}
					} catch (SQLException e) {
					}
				}
				//被邀请人应答邀请
			} else if (decline != null && decline.attribute("to") != null
					&& decline.attribute("result") != null) {
				String inviter = decline.attributeValue("to");
				String result = decline.attributeValue("result");
				if (!isStringEmpty(inviter) && !isStringEmpty(result) 
						&& (Constants.INVITE_ACCEPT.equals(result) || Constants.INVITE_DENIED.equals(result))) {
					try {
						if (localGroup.isOwner(inviter) 
								&& !localGroup.isMember(message.getFrom().toBareJID())) {
							//处理邀请应答
							String reason = decline.elementText("reason");
							if (Constants.INVITE_ACCEPT.equals(result)) {
								//插入成员
								GroupMember member = new GroupMember(group_id, from);
								member.setMember_nick(message.getFrom().getNode());
								member.setRole(GroupMember.ROLE_DEFAULT);
								dbManager.insertGroupMember(member);
								//发送消息给邀请人
								Message msg = packetUtil.createInviteResultMessage(localGroup.getGroup(), inviter, from, reason, result);
								sendSysMessage(msg, new JID(inviter));
								//发送在线消息给群成员
								sendPacket(localGroup.getGroupMembers().keySet(), packetUtil.createNewMemberPresence(new GroupApply(group_id, from)));
								
								boolean is_creator = localGroup.isOwner(getAddress().toBareJID());
								LocalGroupRole role = new LocalGroupRole(server, localGroup, this, 
										is_creator ? Role.moderator : Role.participant, 
												is_creator ? Affiliation.owner : Affiliation.member);
								localGroup.addMember(role);
								addGroupRole(localGroup.getGroup().getId(), role);
								
							} else if (Constants.INVITE_DENIED.equals(result)) {
								//发送消息给邀请人
								Message msg = packetUtil.createInviteResultMessage(localGroup.getGroup(), inviter, from, reason, result);
								sendSysMessage(msg, new JID(inviter));
//								if (!sendPacketIfOnline(msg, inviter)) {
//									GroupSysMessage sysMessage = new GroupSysMessage(inviter, msg.toXML());
//									dbManager.insertGroupSysMessage(sysMessage);
//								}
								//更新消息状态
							}
						}
					} catch (SQLException e) {
					}
				}
			}
		}
	}
	
	/**
	 * 发送群聊消息
	 * 
	 * @param message
	 * @return
	 */
	private void sendGroupMessage(Message message) {
		String from = message.getFrom().toBareJID();
		JID to = message.getTo();
		long group_id;
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		LocalGroup localGroup = server.getGroup(group_id);
		try {
			//需要检查群是否存在,发送者是否是群成员
			if (localGroup != null && localGroup.isMember(message.getFrom().toBareJID())) {
				GroupMessage groupMessage = new GroupMessage(group_id, from, message.getBody());
				dbManager.insertGroupMessage(groupMessage);
				message.setFrom(to.toBareJID() + "/" + message.getFrom().getNode());
				sendPacket(localGroup.getGroupMembers().keySet(), message);
			}
		} catch (SQLException e) {
		}
	}
	
	/**
	 * 发送私聊消息
	 * 
	 * @param message
	 */
	private void sendPrivateMessage(Message message) {
//		<message from='user1@352.cn' to='13@group.352.cn' type='chat'>
//		    <group to='user2@352.cn' />
//		    <body><![CDATA[hello world.]]</body>
//		</message>
		String sender = message.getFrom().toBareJID();
		JID to = message.getTo();
		Element group_e = message.getElement().element(TAG.GROUP);
		String receiver = group_e.attributeValue(ATTRIBUTE_TAG.TO);
		if (isStringEmpty(receiver))
			return;
		long group_id;
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		LocalGroup localGroup = server.getGroup(group_id);
		try {
			//需要检查群是否存在,发送者和接收者是否是群成员
			if (localGroup != null && localGroup.isMember(message.getFrom().toBareJID())) {
				//发送消息
				GroupPriMessage priMessage = new GroupPriMessage(sender, receiver, group_id, message.getBody());
				dbManager.insertGroupPriMessage(priMessage);
				Message msg = packetUtil.createGroupPriMessage(sender, receiver, group_id, message.getBody());
				sendPacketIfOnline(msg, new JID(receiver));
			}
		} catch (SQLException e) {
		}
	}
	
	private void putMapNotEmpty(Map<String, String> map, Element element, String key) {
		String value = element.attributeValue(key);
		if (value != null && !"".equals(value.trim()))
			map.put(key, value);
	}
	
	/**
	 * 字符串为空或空串
	 * 
	 * @param s
	 * @return
	 */
	private boolean isStringEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
	

	private void sendSysMessage(Message msg, JID receiver) throws SQLException {
		if (!sendPacketIfOnline(msg, receiver)) {
			GroupSysMessage sysMessage = new GroupSysMessage(receiver.toBareJID(), msg.toXML());
			dbManager.insertGroupSysMessage(sysMessage);
		}
	}
	
	/**
	 * 发送packet给在线用户
	 * 
	 * @param packet
	 * @param jid
	 */
	private boolean sendPacketIfOnline(Packet packet, JID receiver) {
		boolean sent = false;
		Collection<ClientSession> sessions = SessionManager.getInstance().getSessions(receiver.getNode());
		if (sessions != null && !sessions.isEmpty()) {
			for (ClientSession session : sessions) {
				packet.setTo(session.getAddress());
				router.route(packet);
				sent = true;
			}
		}
		return sent;
	}
	
	private void sendPacket(Set<String> members, Packet packet) {
		sendPacket(members, packet, null);
	}
	
	/**
	 * 批量发送
	 * 
	 * @param list
	 * @param packet
	 * @param except
	 */
	private void sendPacket(Set<String> members, Packet packet, String except) {
		if (members != null && !members.isEmpty()) {
			for (String member_jid : members) {
				Collection<ClientSession> sessions = SessionManager.getInstance().getSessions(member_jid.split("@")[0]);
				if (sessions != null && !sessions.isEmpty()) {
					for (ClientSession session : sessions) {
						String to = session.getAddress().toString();
						if (except == null || !except.equals(to)) {
							packet.setTo(session.getAddress());
							packetUtil.sendPacket(packet.createCopy(), ComponentManagerFactory.getComponentManager());
						}
					}
				}
			}
		}
	}
	
	private void sendErrorPacket(Packet packet, PacketError.Condition error) {
		if (packet instanceof IQ) {
			IQ reply = IQ.createResultIQ((IQ) packet);
			reply.setChildElement(((IQ) packet).getChildElement().createCopy());
			reply.setError(error);
			router.route(reply);
		} else {
			Packet reply = packet.createCopy();
			reply.setError(error);
			reply.setFrom(packet.getTo());
			reply.setTo(packet.getFrom());
			router.route(reply);
		}
	}
}