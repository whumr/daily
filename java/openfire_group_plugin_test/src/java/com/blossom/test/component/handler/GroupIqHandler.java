package com.blossom.test.component.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.ATTRIBUTE_VALUE;
import com.blossom.test.component.Constants.NAMESPACE;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupSysMessage;
import com.blossom.test.util.GroupError;

public class GroupIqHandler extends GroupPacketHandler {

	/**
	 * ����iq
	 * 
	 * @param iq
	 */
	public void processIq(IQ iq) {
		IQ reply = handleIQ(iq);
		if (reply != null)
			sendPacket(reply);
	}
	
	/**
	 * ����iq
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleIQ(IQ iq) {
		Type type = iq.getType();
		Element childElement = iq.getChildElement();
		String namespace = null;
		if (childElement != null)
			namespace = childElement.getNamespaceURI();
		//get����
		if (Type.get == type) {
			if (childElement != null) {
				if (TAG.QUERY.equals(childElement.getName())) {
					//��ѯȺ�б�
//					if (NAMESPACE.DISCO_ITEMS.equals(namespace))
//						return searchGroups(iq);
					//�������Ⱥ
					if (NAMESPACE.JABBER_REGISTER.equals(namespace))
						return applyUserGroup(iq);
					//muc_admin, get����
					if (NAMESPACE.MUC_ADMIN.equals(namespace)) 
						return handleMucAdminGet(iq);
				}
			}
		//set����
		} else if (Type.set == type) {
			if (childElement != null) {
				if (TAG.QUERY.equals(childElement.getName())) {
					//muc#owner,set����
					if (NAMESPACE.MUC_OWNER.equals(namespace)) 
						return handleMucOwnerSet(iq);
					//��Ⱥ���
					if (NAMESPACE.JABBER_REGISTER.equals(namespace))
						return handleRegisterSet(iq);
					//�߳�Ⱥ��Ա
					if (NAMESPACE.MUC_ADMIN.equals(namespace)) 
						return kickGroupMember(iq);
				}
			}
		}
		return null;
	}
	
	/**
	 * http://jabber.org/protocol/muc#owner
	 * ����muc#owner set����
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleMucOwnerSet(IQ iq) {
		Element query = iq.getChildElement();
		//ȡxԪ��
		Element x = query.element(TAG.X);
//		//�½�Ⱥ
//		if (x != null && ATTRIBUTE_VALUE.X_TYPE_SUBMIT.equals(x.attributeValue(ATTRIBUTE_TAG.TYPE))
//				&& NAMESPACE.JABBER_X_DATA.equals(x.getNamespaceURI()))
//			return createGroup(iq);
		//ȡdestroyԪ��
		Element destroy = query.element(TAG.DESTROY);
		//ɾ��Ⱥ
		if (destroy != null) {
			String group = destroy.attributeValue(ATTRIBUTE_TAG.JID);
			if (group != null) 
				return deleteGroup(iq);
		}
		return null;
	}
	
//	/**
//	 * ����Ⱥ
//	 * 
//	 * @param iq
//	 * @return
//	 */
//	private IQ createGroup(IQ iq) {
//		JID from = iq.getFrom();
//		String group_name = iq.getChildElement().elementText(TAG.X);
//		if (group_name == null)
//			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
//		Group group = new Group(from.toBareJID(), group_name);
//		try {
//			long id = groupDbManager.insertGroup(group);
//			IQ reply = IQ.createResultIQ(iq);
//			reply.setFrom(id + "@" + getServerDomain());
//			return reply;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
//		}
//	}
	
	/**
	 * ɾ��Ⱥ
	 * 
	 * @param iq
	 * @return
	 */
	private IQ deleteGroup(IQ iq) {
		String group = iq.getChildElement().element(TAG.DESTROY)
				.attributeValue(ATTRIBUTE_TAG.JID);
		String reason = iq.getChildElement().element(TAG.DESTROY).elementText(TAG.REASON);
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(new JID(group).getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//ȡ��Ա�б�
			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
			//ɾ��Ⱥ
			groupDbManager.deleteGroupById(group_id);
			//����ɾ����Ϣ
			sendPacket(list, packetUtil.createGroupDeletePresence(group_id, reason));
			//����ɾ�����
			IQ reply = IQ.createResultIQ(iq);
			return reply;
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
//	/**
//	 * ����Ⱥ
//	 * ���û��condition�򷵻��û�����Ⱥ�б�
//	 * 
//	 * @param iq
//	 * @return
//	 */
//	private IQ searchGroups(IQ iq) {
//		JID from = iq.getFrom();
//		Element condition = iq.getChildElement().element(TAG.CONDITION);
//		List<Group> list = null;
//		IQ reply = null;
//		try {
//			//����������ѯ
//			if (condition != null) {
//				String params = condition.getText();
//				Map<String, String> map = parseParams(params);
//				list = groupDbManager.searchGroups(map);
//				reply = packetUtil.createGroupSearchResult(iq, list, true);
//			//û��������ѯ�û�������
//			} else {
//				list = groupDbManager.getGroupsByUserName(from.toBareJID());
//				reply = packetUtil.createGroupSearchResult(iq, list, false);
//			}
//		} catch (SQLException e) {
//			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
//		}
//        return reply;
//	}
	
	/**
	 * ����url��ʽ�Ĳ���
	 * e.g a=xx&b=aa
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, String> parseParams(String params) {
		Map<String, String> map = new HashMap<String, String>();
		if (params == null || "".equals(params.trim()))
			return map;
		String array[] = params.split("&");
		for (String s : array) {
			int index = s.indexOf('=');
			if (index > 0) {
				String key = s.substring(0, index).trim();
				String value = s.substring(index + 1).trim();
				if (!"".equals(key) && !"".equals(value))
					map.put(key, value);
			}
		}
		return map;
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
			String reason = iq.getChildElement().elementText(TAG.REASON);
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
	 * ����register set
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleRegisterSet(IQ iq) {
		Element query = iq.getChildElement();
		if (query.element(TAG.QUIT) != null)
			return quitGroup(iq);
		else
			return processGroupApply(iq);
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
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(group.getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		//ɾ��Ⱥ��Ա
		try {
			List<GroupMember> members = groupDbManager.listGroupMembers(group_id);
			if (members != null && !members.isEmpty()) {
				int delete = groupDbManager.deleteGroupMember(group_id, member_jid);
				//�м�¼��ɾ��,֪ͨȺ��Ա
				if (delete > 0) {
					//���Ⱥ���˳���ɾ��Ⱥ
					boolean creater_quit = false;
					for (GroupMember member : members) {
						if (member_jid.equals(member.getMember_jid())) {
							creater_quit = GroupMember.ROLE_CREATOR.equals(member.getRole());
							break;
						}
					}
					if (creater_quit)
						groupDbManager.deleteGroupById(group_id);
					//������Ϣ
					sendPacket(members, packetUtil.createQuitPresence(member_jid, group_id));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
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
//		<iq type='set' from='user2@352.cn' to='13@group.352.cn'>
//		<query xmlns='jabber:iq:register'/>
//		   <applier>user1@352.cn</applier>
//		   <result>1\2</result>
//		</query>
//		</iq>
		//������
		String creator = iq.getFrom().toBareJID();
		Element query = iq.getChildElement();
		//������
		String applier = query.elementText(TAG.APPLIER);
		//�������
		String result = query.elementText(TAG.RESULT);
		if (result == null || applier == null || 
				(!GroupApply.STATUS_ACCEPT.equals(result) && !GroupApply.STATUS_DENIED.equals(result)))
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		//ȡȺid
		long group_id = -1;
		try {
			group_id = Long.parseLong(iq.getTo().getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//����Ƿ��Ѿ��������������
			GroupApply apply = groupDbManager.getGroupApplyByUserGroupId(group_id, applier);
			if (apply == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.apply_already_processed);
			Group group = groupDbManager.getGroupById(group_id);
			//Ⱥ������
			if(group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//��鷢�����Ƿ���Ȩ��
			if (!creator.equals(group.getCreator()))
				return packetUtil.createErrorIq(iq, GroupError.Condition.no_permission);
			//��������
			apply.setStatus(result);
			groupDbManager.processGroupApply(apply);
			
			//������������ߣ�������Ϣ֪ͨ
			Message applier_msg = packetUtil.createGroupApplyResponseMessage(apply, group);
			boolean sent = sendPacketIfOnline(applier_msg, apply.getApply_user());
			//δ���ͣ����浽���ݿ�
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(apply.getApply_user(), applier_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//�����ͬ��,����presence��Ⱥ��Ա
            if (GroupApply.STATUS_ACCEPT.equals(apply.getStatus())) {
        		//ȡ��Ա�б�
    			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
    			//������Ϣ
    			sendPacket(list, packetUtil.createNewMemberPresence(apply));
            }			
			//�������
			return IQ.createResultIQ(iq);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * ����muc_admin get
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleMucAdminGet(IQ iq) {
		Element item = iq.getChildElement().element(TAG.ITEM);
		//��ȡȺ��Ա
		if (item != null && ATTRIBUTE_VALUE.MEMBER.equals(item.attributeValue(ATTRIBUTE_TAG.AFFILIATION)))
			return listGroupMembers(iq);
		return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
	}
	
	/**
	 * ��ȡȺ��Ա
	 *  
	 * @param iq
	 * @return
	 */
	private IQ listGroupMembers(IQ iq) {
		long group_id;
		try {
			group_id = Long.parseLong(iq.getTo().getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//��ѯȺ��Ա
			List<GroupMember> members = groupDbManager.listGroupMembers(group_id);
			//����Ƿ�����
			for (GroupMember member : members)
				member.setOnline(isUserOnline(member.getMember_jid()));
			return packetUtil.createMemberQueryResponse(iq, members);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * �߳�Ⱥ��Ա
	 * 
	 * @param iq
	 * @return
	 */
	private IQ kickGroupMember(IQ iq) {
		//ȡȺid
		long group_id;
		try {
			group_id = Long.parseLong(iq.getTo().getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		Element item = iq.getChildElement().element(TAG.ITEM);
		if (item == null || item.attributeValue(ATTRIBUTE_TAG.JID) == null) 
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		String member_jid = item.attributeValue(ATTRIBUTE_TAG.JID);
		String from = iq.getFrom().toBareJID();
		//����ɾ���Լ�
		if (member_jid.equals(from))
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		String reason = iq.getChildElement().elementText(TAG.REASON);
		try {
			Group group = groupDbManager.getGroupById(group_id);
			//Ⱥ������
			if (group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//û��Ȩ��
			if (!from.equals(group.getCreator()))	
				return packetUtil.createErrorIq(iq, GroupError.Condition.no_permission);
			//�����ڸó�Ա
			if (!groupDbManager.isGroupMember(group_id, member_jid)) 
				return packetUtil.createErrorIq(iq, GroupError.Condition.member_not_exsist);
			//ɾ����Ա
			groupDbManager.deleteGroupMember(group_id, member_jid);
			//֪ͨ��ɾ����Ա
			Message kick_msg = packetUtil.createKickMemberResponseMessage(member_jid, group, reason);
			boolean sent = sendPacketIfOnline(kick_msg, member_jid);
			//δ���ͣ����浽���ݿ�
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(from, member_jid, kick_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//֪ͨȺ��Ա�����б�
    		//ȡ��Ա�б�
			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
			//֪ͨ��ɾ����Ա
			list.add(new GroupMember(group_id, member_jid));
			//����ɾ����Ϣ
			sendPacket(list, packetUtil.createMemberOutPresence(member_jid, group));
			//�������
			return IQ.createResultIQ(iq);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
}
