package com.blossom.test.component.handler;

import java.sql.SQLException;
import java.util.List;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;

import com.blossom.test.component.Constants;
import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.NAMESPACE;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupMessage;
import com.blossom.test.entity.GroupPriMessage;
import com.blossom.test.entity.GroupSysMessage;

public class GroupMessageHandler extends GroupPacketHandler {
	
	public void processMessage(Message message) {
		Type type = message.getType();
		//Ⱥ��
		if (type == Type.groupchat) {
			//��Ⱥ����
			if (message.getElement().element(TAG.SUBJECT) != null)
				renameGroup(message);
			//�����û�����
			else if (message.getChildElement(TAG.X, NAMESPACE.MUC_USER) != null)
				inviteMember(message);
			//����Ⱥ����Ϣ
			else if (message.getBody() != null)
				sendGroupMessage(message);
		//˽��
		} else if (type == Type.chat) {
			//Ⱥ��Ա˽��
			if (message.getElement().element(TAG.GROUP) != null)
				sendPrivateMessage(message);
		}
	}
	
	/**
	 * ������Ⱥ
	 * 
	 * @param message
	 */
	private void renameGroup(Message message) {
		String from = message.getFrom().toBareJID();
		JID to = message.getTo();
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		String new_name = message.getElement().element(TAG.SUBJECT).getText();
		//�����ֲ�Ϊ��
		if (new_name != null && !"".equals(new_name.trim())) {
			try {
				//����Ƿ���Ȩ��
				Group group = groupDbManager.getGroupById(group_id);
				if (group != null && from.equals(group.getCreator())) {
					//����
					groupDbManager.renameGroup(group_id, new_name);
					//֪ͨȺ��Ա
					List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
					sendPacket(list, packetUtil.createRenameMessage(group_id, new_name, from));
				}
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * �����Ա����
	 * 
	 * @param message
	 */
	private void inviteMember(Message message) {
		Element x = message.getChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element invite = x.element(TAG.INVITE);
		Element decline = x.element(TAG.DECLINE);
		//from
		String from = message.getFrom().toBareJID();
		JID to = message.getTo();
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return;
		}
		//Ⱥ�������û�
		if (invite != null && invite.attribute(ATTRIBUTE_TAG.TO) != null) {
			String invitee = invite.attributeValue(ATTRIBUTE_TAG.TO);
			if (!isStringEmpty(invitee)) {
				try {
					//ȡȺ��Ϣ
					Group group = groupDbManager.getGroupById(group_id);
					if (group != null && from.equals(group.getCreator())
							&& !groupDbManager.isGroupMember(group_id, invitee)) {
						String reason = invite.elementText(TAG.REASON);
						//����ϵͳ��Ϣ
						Message msg = packetUtil.createInviteMessage(group, from, invitee, reason);
						if (!sendPacketIfOnline(msg, invitee)) {
							GroupSysMessage sysMessage = new GroupSysMessage(invitee, msg.toXML());
							groupDbManager.insertGroupSysMessage(sysMessage);
						}
					}
				} catch (SQLException e) {
				}
			}
		//��������Ӧ������
		} else if (decline != null && decline.attribute(ATTRIBUTE_TAG.TO) != null
				&& decline.attribute(ATTRIBUTE_TAG.RESULT) != null) {
			String inviter = decline.attributeValue(ATTRIBUTE_TAG.TO);
			String result = decline.attributeValue(ATTRIBUTE_TAG.RESULT);
			if (!isStringEmpty(inviter) && !isStringEmpty(result) 
					&& (Constants.INVITE_ACCEPT.equals(result) || Constants.INVITE_DENIED.equals(result))) {
				try {
					Group group = groupDbManager.getGroupById(group_id);
					if (group != null && inviter.equals(group.getCreator())
							&& !groupDbManager.isGroupMember(group_id, from)) {
						//��������Ӧ��
						String reason = decline.elementText(TAG.REASON);
						if (Constants.INVITE_ACCEPT.equals(result)) {
							//�����Ա
							GroupMember member = new GroupMember(group_id, from);
							member.setMember_nick(from.split("@")[0]);
							member.setRole(GroupMember.ROLE_DEFAULT);
							groupDbManager.insertGroupMember(member);
							//������Ϣ��������
							Message msg = packetUtil.createInviteResultMessage(group, inviter, from, reason, result);
							if (!sendPacketIfOnline(msg, inviter)) {
								GroupSysMessage sysMessage = new GroupSysMessage(inviter, msg.toXML());
								groupDbManager.insertGroupSysMessage(sysMessage);
							}
							//����������Ϣ��Ⱥ��Ա
			        		//ȡ��Ա�б�
			    			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
			    			//����ɾ����Ϣ
			    			sendPacket(list, packetUtil.createNewMemberPresence(new GroupApply(group_id, from)));
						} else if (Constants.INVITE_DENIED.equals(result)) {
							//������Ϣ��������
							Message msg = packetUtil.createInviteResultMessage(group, inviter, from, reason, result);
							if (!sendPacketIfOnline(msg, inviter)) {
								GroupSysMessage sysMessage = new GroupSysMessage(inviter, msg.toXML());
								groupDbManager.insertGroupSysMessage(sysMessage);
							}
							//������Ϣ״̬
						}
					}
				} catch (SQLException e) {
				}
			}
		}
	}
	
	/**
	 * ����Ⱥ����Ϣ
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
		try {
			//��Ҫ���Ⱥ�Ƿ����,�������Ƿ���Ⱥ��Ա
			Group group = groupDbManager.getGroupById(group_id);
			if (group != null && groupDbManager.isGroupMember(group_id, from)) {
				GroupMessage groupMessage = new GroupMessage(group_id, from, message.getBody());
				groupDbManager.insertGroupMessage(groupMessage);
				List<GroupMember> members = groupDbManager.listGroupMembers(group_id);
				message.setFrom(to.toBareJID() + "/" + message.getFrom().getNode());
				sendPacket(members, message);
			}
		} catch (SQLException e) {
		}
	}
	
	/**
	 * ����˽����Ϣ
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
		try {
			//��Ҫ���Ⱥ�Ƿ����,�����ߺͽ������Ƿ���Ⱥ��Ա
			Group group = groupDbManager.getGroupById(group_id);
			if (group != null && groupDbManager.isGroupMember(group_id, sender)
					&& groupDbManager.isGroupMember(group_id, receiver)) {
				//������Ϣ
				GroupPriMessage priMessage = new GroupPriMessage(sender, receiver, group_id, message.getBody());
				groupDbManager.insertGroupPriMessage(priMessage);
				Message msg = packetUtil.createGroupPriMessage(sender, receiver, group_id, message.getBody());
				sendPacketIfOnline(msg, receiver);
			}
		} catch (SQLException e) {
		}
	}
}
