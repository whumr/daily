package com.blossom.test.component.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.PresenceEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.blossom.test.component.Config;
import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupSysMessage;
import com.blossom.test.util.PacketUtil;

public class GroupMemberListener implements PresenceEventListener {
	
	private static final Logger Log = LoggerFactory.getLogger(GroupMemberListener.class);
	
	private GroupDbManager groupDbManager;
	private ComponentManager componentManager;
	private PacketUtil packetUtil;
	
	public GroupMemberListener() {
		groupDbManager = GroupDbManager.getInstance();
		componentManager = ComponentManagerFactory.getComponentManager();
		packetUtil = PacketUtil.getInstance();
	}

	@Override
	public void availableSession(ClientSession session, Presence presence) {
		//用户上线
		JID user_jid = session.getAddress();
		//取系统消息
		pushSysMessage(user_jid);
		//通知群成员上线信息
		if (Config.LOGON_NOTICE_GROUP)
			pushPresenceMessage(user_jid, true);
		//取群消息
	}
	
	/**
	 * 推送系统消息
	 * 
	 * @param jid
	 */
	private void pushSysMessage(JID jid) {
		try {
			List<GroupSysMessage> list = groupDbManager.getUnsendSysMessage(jid.toBareJID());
			for (GroupSysMessage sysMessage : list) {
				String msg = sysMessage.getContent();
				if (msg != null && !"".equals(msg.trim())) {
					try {
						Element e = DocumentHelper.parseText(msg).getRootElement();
						Packet packet = null;
						if ("iq".equalsIgnoreCase(e.getName()))
							packet = new IQ(e);
						else if ("message".equalsIgnoreCase(e.getName()))
							packet = new Message(e);
						else if ("presence".equalsIgnoreCase(e.getName()))
							packet = new Presence(e);
						if (packet != null)	{
							packet.setTo(jid);
							packetUtil.sendPacket(packet, componentManager);
						}
					} catch (DocumentException e) {
						Log.error("parse message error:" + msg, e);
					}
				}
			}
		} catch (SQLException e) {
		}
	}
	
	/**
	 * 发送上线通知给用户所在的群成员
	 * 
	 * @param jid
	 */
	private void pushPresenceMessage(JID jid, boolean on_line) {
		try {
			String member_jid = jid.toBareJID();
			//取成员
			List<GroupMember> list = groupDbManager.getAllGroupMemberByMember(member_jid);
			//发送消息
			if (list != null && !list.isEmpty()) {
				//按群分组
				Map<Long, List<GroupMember>> member_map = new HashMap<Long, List<GroupMember>>();
				Map<Long, GroupMember> role_map = new HashMap<Long, GroupMember>();
				for (GroupMember member : list) {
					if (member_jid.equals(member.getMember_jid()))
						role_map.put(member.getGroup_id(), member);
					else {
						long group_id = member.getGroup_id();
						if (member_map.containsKey(group_id))
							member_map.get(group_id).add(member);
						else {
							List<GroupMember> tmp_list = new ArrayList<GroupMember>();
							tmp_list.add(member);
							member_map.put(group_id, tmp_list);
						}
					}
				}
				//按群发送消息
				for (Long group_id : role_map.keySet()) {
					//如果有其它成员，发送上线通知
					if (member_map.containsKey(group_id)) 
						packetUtil.sendMemberOnOfflinePresence(role_map.get(group_id), member_map.get(group_id), on_line);
				}
			}
		} catch (SQLException e) {
		}
	}

	@Override
	public void unavailableSession(ClientSession session, Presence presence) {
		//用户离线
		JID user_jid = session.getAddress();
		//通知群成员上线信息
		if (Config.LOGOUT_NOTICE_GROUP)
			pushPresenceMessage(user_jid, false);
	}

	@Override
	public void presenceChanged(ClientSession session, Presence presence) {
	}

	@Override
	public void subscribedToPresence(JID subscriberJID, JID authorizerJID) {
	}

	@Override
	public void unsubscribedToPresence(JID unsubscriberJID, JID recipientJID) {
	}

}
