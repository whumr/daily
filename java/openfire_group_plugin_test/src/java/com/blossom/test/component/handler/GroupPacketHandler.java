package com.blossom.test.component.handler;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.session.ClientSession;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.Packet;

import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.util.PacketUtil;

public class GroupPacketHandler {

	protected GroupDbManager groupDbManager;
	protected SessionManager sessionManager;
	protected PacketUtil packetUtil;
	
	public GroupPacketHandler() {
		this.groupDbManager = GroupDbManager.getInstance();
		this.sessionManager = SessionManager.getInstance();
		this.packetUtil = PacketUtil.getInstance();
	}
	
	/**
	 * 发送packet
	 * 
	 * @param packet
	 */
	protected void sendPacket(Packet packet) {
		packetUtil.sendPacket(packet, ComponentManagerFactory.getComponentManager());
	}
	
	/**
	 * 发送packet给在线用户
	 * 
	 * @param packet
	 * @param jid
	 */
	protected boolean sendPacketIfOnline(Packet packet, String receiver) {
		boolean sent = false;
		Collection<ClientSession> sessions = sessionManager.getSessions(receiver.split("@")[0]);
		if (sessions != null && !sessions.isEmpty()) {
			for (ClientSession session : sessions) {
				packet.setTo(session.getAddress());
				packetUtil.sendPacket(packet.createCopy(), ComponentManagerFactory.getComponentManager());
				sent = true;
			}
		}
		return sent;
	}
	
	/**
	 * 批量发送
	 * 
	 * @param list
	 * @param packet
	 * @param except
	 */
	protected void sendPacket(List<GroupMember> list, Packet packet, String except) {
		if (list != null && !list.isEmpty()) {
			for (GroupMember groupMember : list) {
				String member_jid = groupMember.getMember_jid();
				Collection<ClientSession> sessions = sessionManager.getSessions(member_jid.split("@")[0]);
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

	protected void sendPacket(List<GroupMember> list, Packet packet) {
		sendPacket(list, packet, null);
	}
	
	/**
	 * 判断用户是否在线
	 * 
	 * @param jid
	 * @return
	 */
	protected boolean isUserOnline(String jid) {
		Collection<ClientSession> sessions = sessionManager.getSessions(jid.split("@")[0]);
		return (sessions != null && !sessions.isEmpty());
	}
	
	/**
	 * 字符串为空或空串
	 * 
	 * @param s
	 * @return
	 */
	protected boolean isStringEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
	
	/**
	 * 取组件域名
	 * 
	 * @return
	 */
	protected String getServerDomain() {
		return packetUtil.getServerDomain();
	}
}
