package com.blossom.muc.handler;

import java.util.Collection;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.session.ClientSession;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.Packet;

import com.blossom.muc.GroupServiceImpl;
import com.blossom.muc.LocalGroup;
import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.util.PacketUtil;

public class BaseHandler {

	protected GroupDbManager groupDbManager;
	protected SessionManager sessionManager;
	protected PacketUtil packetUtil;
	protected GroupServiceImpl service;
	protected long sevice_id;
	
	public BaseHandler(GroupServiceImpl service) {
		this.groupDbManager = GroupDbManager.getInstance();
		this.sessionManager = SessionManager.getInstance();
		this.packetUtil = PacketUtil.getInstance();
		this.service = service;
		this.sevice_id = XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatServiceID(service.getServiceName());
	}
	
	/**
	 * ����packet
	 * 
	 * @param packet
	 */
	protected void sendPacket(Packet packet) {
		packetUtil.sendPacket(packet, ComponentManagerFactory.getComponentManager());
	}
	
	/**
	 * ����packet�������û�
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
	 * ��������
	 * 
	 * @param list
	 * @param packet
	 * @param except
	 */
	protected void sendPacket(Collection<String> list, Packet packet, String except) {
		if (list != null && !list.isEmpty()) {
			for (String jid : list) {
				Collection<ClientSession> sessions = sessionManager.getSessions(jid.split("@")[0]);
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

	protected void sendPacket(Collection<String> list, Packet packet) {
		sendPacket(list, packet, null);
	}
	
	/**
	 * �ж��û��Ƿ�����
	 * 
	 * @param jid
	 * @return
	 */
	protected boolean isUserOnline(String jid) {
		Collection<ClientSession> sessions = sessionManager.getSessions(jid.split("@")[0]);
		return (sessions != null && !sessions.isEmpty());
	}
	
	/**
	 * �ַ���Ϊ�ջ�մ�
	 * 
	 * @param s
	 * @return
	 */
	protected boolean isStringEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
	
	/**
	 * ȡ�������
	 * 
	 * @return
	 */
	protected String getServerDomain() {
		return packetUtil.getServerDomain();
	}

	/**
	 * ��ȡ����
	 * @param jid
	 * @return
	 */
	protected LocalGroup getRoom(String jid) {
		
		return null;
	}
}
