package com.blossom.muc.handler;

import java.sql.SQLException;
import java.util.Map;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketError;

import com.blossom.muc.GroupServiceImpl;
import com.blossom.muc.LocalGroup;
import com.blossom.muc.LocalGroupRole;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupSysMessage;
import com.blossom.test.util.GroupError;

public class GroupIQRegisterHandler extends BaseHandler {

	public GroupIQRegisterHandler(GroupServiceImpl service) {
		super(service);
	}

	public IQ handleIQ(IQ iq) {
		Type type = iq.getType();
		String group_id = iq.getTo().getNode();
		if (group_id == null)
			return null;
		//������Ⱥ
		if (Type.get == type)
			return applyUserGroup(iq);
		else if (Type.set == type) {
			Element element = iq.getChildElement();
			Element quit = element.element("quit");
			if (quit != null)
				return quitGroup(iq);
			else 
				return processGroupApply(iq);
		}
		return null;
	}
	
	/**
	 * �û�������Ⱥ
	 * 
	 * @param iq
	 * @return
	 */
	private IQ applyUserGroup(IQ iq) {
		JID from = iq.getFrom();
		JID to = iq.getTo();
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//��������
			String reason = iq.getChildElement().elementText("reason");
			//������
			String member_jid = from.toBareJID();
			Group group = groupDbManager.getGroupById(group_id);
			//Ⱥ������
			if (group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//�Ѿ���Ⱥ��Ա
			if (groupDbManager.isGroupMember(group_id, member_jid)) 
				return packetUtil.createErrorIq(iq, GroupError.Condition.alread_in_group);
			//�Ѿ��ύ������
			if (groupDbManager.getGroupApplyByUserGroupId(group_id, member_jid) != null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.alread_applied);
			
			//�½���Ⱥ����
			GroupApply apply = new GroupApply(group_id, member_jid);
			groupDbManager.insertGroupApply(apply);
			
			//������Ҫ���͸�Ⱥ������Ϣ
			IQ to_admin = packetUtil.createGroupApplyMessage(group, member_jid, reason);
			//����ϵͳ��Ϣ
			GroupSysMessage sysMessage = new GroupSysMessage(group_id, member_jid, group.getCreator(), to_admin.toXML());
			groupDbManager.insertGroupSysMessage(sysMessage);
			
			//���Ⱥ�����ߣ����������Ϣ
			sendPacketIfOnline(to_admin, group.getCreator());
			
			return IQ.createResultIQ(iq);
				
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * �˳�Ⱥ
	 * 
	 * @param iq
	 * @return
	 */
	private IQ quitGroup(IQ iq) {
		String member_jid = iq.getFrom().toBareJID();
		JID group = iq.getTo();
		IQ reply = IQ.createResultIQ(iq);
		long group_id = -1;
		LocalGroup localGroup = null;
		try {
			group_id = Long.parseLong(group.getNode());
			localGroup = service.getGroup(group_id);
		} catch (Exception e) {
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
		//ɾ��Ⱥ��Ա
		try {
			Map<String, LocalGroupRole> members = localGroup.getGroupMembers();
			if (members != null && !members.isEmpty()) {
				int delete = groupDbManager.deleteGroupMember(group_id, member_jid);
				//�м�¼��ɾ��,֪ͨȺ��Ա
				if (delete > 0) {
					//���Ⱥ���˳���ɾ��Ⱥ
					if (localGroup.isOwner(member_jid))
						groupDbManager.deleteGroupById(group_id);
					//������Ϣ
					sendPacket(members.keySet(), packetUtil.createQuitPresence(member_jid, group_id));
				}
			}
			localGroup.removeMember(member_jid);
		} catch (SQLException e) {
			reply.setError(PacketError.Condition.internal_server_error);
			return reply;
		}
		return null;
	}
	
	/**
	 * ������Ⱥ����
	 * 
	 * @param iq
	 * @return
	 */
	private IQ processGroupApply(IQ iq) {
		IQ reply = IQ.createResultIQ(iq);
		//������
		String creator = iq.getFrom().toBareJID();
		Element query = iq.getChildElement();
		//������
		String applier = query.elementText("applier");
		//�������
		String result = query.elementText("result");
		if (result == null || applier == null || 
				(!GroupApply.STATUS_ACCEPT.equals(result) && !GroupApply.STATUS_DENIED.equals(result))) {
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
		long group_id = -1;
		LocalGroup localGroup = null;
		try {
			group_id = Long.parseLong(iq.getTo().getNode());
			localGroup = service.getGroup(group_id);
		} catch (Exception e) {
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
		try {
			//����Ƿ��Ѿ��������������
			GroupApply apply = groupDbManager.getGroupApplyByUserGroupId(group_id, applier);
			if (apply == null && !localGroup.isOwner(creator)) {
				reply.setError(PacketError.Condition.bad_request);
				return reply;
			}
			//��������
			apply.setStatus(result);
			groupDbManager.processGroupApply(apply);
			//������������ߣ�������Ϣ֪ͨ
			Message applier_msg = packetUtil.createGroupApplyResponseMessage(apply, localGroup.getGroup());
			boolean sent = sendPacketIfOnline(applier_msg, apply.getApply_user());
			//δ���ͣ����浽���ݿ�
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(apply.getApply_user(), applier_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//�����ͬ��,����presence��Ⱥ��Ա
            if (GroupApply.STATUS_ACCEPT.equals(apply.getStatus())) {
    			//������Ϣ
    			sendPacket(localGroup.getGroupMembers().keySet(), packetUtil.createNewMemberPresence(apply));
            }			
			//�������
		} catch (SQLException e) {
			reply.setError(PacketError.Condition.internal_server_error);
		}
		return reply;
	}
}