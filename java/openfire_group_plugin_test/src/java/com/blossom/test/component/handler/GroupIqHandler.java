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
	 * 处理iq
	 * 
	 * @param iq
	 */
	public void processIq(IQ iq) {
		IQ reply = handleIQ(iq);
		if (reply != null)
			sendPacket(reply);
	}
	
	/**
	 * 处理iq
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
		//get请求
		if (Type.get == type) {
			if (childElement != null) {
				if (TAG.QUERY.equals(childElement.getName())) {
					//查询群列表
//					if (NAMESPACE.DISCO_ITEMS.equals(namespace))
//						return searchGroups(iq);
					//申请加入群
					if (NAMESPACE.JABBER_REGISTER.equals(namespace))
						return applyUserGroup(iq);
					//muc_admin, get操作
					if (NAMESPACE.MUC_ADMIN.equals(namespace)) 
						return handleMucAdminGet(iq);
				}
			}
		//set请求
		} else if (Type.set == type) {
			if (childElement != null) {
				if (TAG.QUERY.equals(childElement.getName())) {
					//muc#owner,set操作
					if (NAMESPACE.MUC_OWNER.equals(namespace)) 
						return handleMucOwnerSet(iq);
					//入群审核
					if (NAMESPACE.JABBER_REGISTER.equals(namespace))
						return handleRegisterSet(iq);
					//踢出群成员
					if (NAMESPACE.MUC_ADMIN.equals(namespace)) 
						return kickGroupMember(iq);
				}
			}
		}
		return null;
	}
	
	/**
	 * http://jabber.org/protocol/muc#owner
	 * 处理muc#owner set操作
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleMucOwnerSet(IQ iq) {
		Element query = iq.getChildElement();
		//取x元素
		Element x = query.element(TAG.X);
//		//新建群
//		if (x != null && ATTRIBUTE_VALUE.X_TYPE_SUBMIT.equals(x.attributeValue(ATTRIBUTE_TAG.TYPE))
//				&& NAMESPACE.JABBER_X_DATA.equals(x.getNamespaceURI()))
//			return createGroup(iq);
		//取destroy元素
		Element destroy = query.element(TAG.DESTROY);
		//删除群
		if (destroy != null) {
			String group = destroy.attributeValue(ATTRIBUTE_TAG.JID);
			if (group != null) 
				return deleteGroup(iq);
		}
		return null;
	}
	
//	/**
//	 * 创建群
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
	 * 删除群
	 * 
	 * @param iq
	 * @return
	 */
	private IQ deleteGroup(IQ iq) {
		String group = iq.getChildElement().element(TAG.DESTROY)
				.attributeValue(ATTRIBUTE_TAG.JID);
		String reason = iq.getChildElement().element(TAG.DESTROY).elementText(TAG.REASON);
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(new JID(group).getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//取成员列表
			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
			//删除群
			groupDbManager.deleteGroupById(group_id);
			//发送删除消息
			sendPacket(list, packetUtil.createGroupDeletePresence(group_id, reason));
			//反馈删除结果
			IQ reply = IQ.createResultIQ(iq);
			return reply;
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
//	/**
//	 * 查找群
//	 * 如果没有condition则返回用户所在群列表
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
//			//有条件，查询
//			if (condition != null) {
//				String params = condition.getText();
//				Map<String, String> map = parseParams(params);
//				list = groupDbManager.searchGroups(map);
//				reply = packetUtil.createGroupSearchResult(iq, list, true);
//			//没条件，查询用户所在组
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
	 * 解析url格式的参数
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
	 * 用户申请入群
	 * 
	 * @param iq
	 * @return
	 */
	private IQ applyUserGroup(IQ iq) {
		JID from = iq.getFrom();
		JID to = iq.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(to.getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//申请理由
			String reason = iq.getChildElement().elementText(TAG.REASON);
			//申请人
			String member_jid = from.toBareJID();
			Group group = groupDbManager.getGroupById(group_id);
			//群不存在
			if (group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//已经是群成员
			if (groupDbManager.isGroupMember(group_id, member_jid)) 
				return packetUtil.createErrorIq(iq, GroupError.Condition.alread_in_group);
			//已经提交了申请
			if (groupDbManager.getGroupApplyByUserGroupId(group_id, member_jid) != null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.alread_applied);
			
			//新建入群申请
			GroupApply apply = new GroupApply(group_id, member_jid);
			groupDbManager.insertGroupApply(apply);
			
			//生成需要发送给群主的消息
			IQ to_admin = packetUtil.createGroupApplyMessage(group, member_jid, reason);
			//插入系统消息
			GroupSysMessage sysMessage = new GroupSysMessage(group_id, member_jid, group.getCreator(), to_admin.toXML());
			groupDbManager.insertGroupSysMessage(sysMessage);
			
			//如果群主在线，发送审核消息
			sendPacketIfOnline(to_admin, group.getCreator());
			
			return IQ.createResultIQ(iq);
				
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * 处理register set
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
	 * 退出群
	 * 
	 * @param iq
	 * @return
	 */
	private IQ quitGroup(IQ iq) {
		String member_jid = iq.getFrom().toBareJID();
		JID group = iq.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(group.getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		//删除群成员
		try {
			List<GroupMember> members = groupDbManager.listGroupMembers(group_id);
			if (members != null && !members.isEmpty()) {
				int delete = groupDbManager.deleteGroupMember(group_id, member_jid);
				//有记录被删除,通知群成员
				if (delete > 0) {
					//如果群主退出，删除群
					boolean creater_quit = false;
					for (GroupMember member : members) {
						if (member_jid.equals(member.getMember_jid())) {
							creater_quit = GroupMember.ROLE_CREATOR.equals(member.getRole());
							break;
						}
					}
					if (creater_quit)
						groupDbManager.deleteGroupById(group_id);
					//发送消息
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
	 * 审批入群申请
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
		//发送者
		String creator = iq.getFrom().toBareJID();
		Element query = iq.getChildElement();
		//申请人
		String applier = query.elementText(TAG.APPLIER);
		//审批结果
		String result = query.elementText(TAG.RESULT);
		if (result == null || applier == null || 
				(!GroupApply.STATUS_ACCEPT.equals(result) && !GroupApply.STATUS_DENIED.equals(result)))
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		//取群id
		long group_id = -1;
		try {
			group_id = Long.parseLong(iq.getTo().getNode());
		} catch (Exception e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		}
		try {
			//检查是否已经处理过这条请求
			GroupApply apply = groupDbManager.getGroupApplyByUserGroupId(group_id, applier);
			if (apply == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.apply_already_processed);
			Group group = groupDbManager.getGroupById(group_id);
			//群不存在
			if(group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//检查发送者是否有权限
			if (!creator.equals(group.getCreator()))
				return packetUtil.createErrorIq(iq, GroupError.Condition.no_permission);
			//处理请求
			apply.setStatus(result);
			groupDbManager.processGroupApply(apply);
			
			//如果申请人在线，发送消息通知
			Message applier_msg = packetUtil.createGroupApplyResponseMessage(apply, group);
			boolean sent = sendPacketIfOnline(applier_msg, apply.getApply_user());
			//未发送，保存到数据库
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(apply.getApply_user(), applier_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//如果是同意,发送presence给群成员
            if (GroupApply.STATUS_ACCEPT.equals(apply.getStatus())) {
        		//取成员列表
    			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
    			//发送消息
    			sendPacket(list, packetUtil.createNewMemberPresence(apply));
            }			
			//处理完成
			return IQ.createResultIQ(iq);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * 处理muc_admin get
	 * 
	 * @param iq
	 * @return
	 */
	private IQ handleMucAdminGet(IQ iq) {
		Element item = iq.getChildElement().element(TAG.ITEM);
		//获取群成员
		if (item != null && ATTRIBUTE_VALUE.MEMBER.equals(item.attributeValue(ATTRIBUTE_TAG.AFFILIATION)))
			return listGroupMembers(iq);
		return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
	}
	
	/**
	 * 获取群成员
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
			//查询群成员
			List<GroupMember> members = groupDbManager.listGroupMembers(group_id);
			//检查是否在线
			for (GroupMember member : members)
				member.setOnline(isUserOnline(member.getMember_jid()));
			return packetUtil.createMemberQueryResponse(iq, members);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
	
	/**
	 * 踢出群成员
	 * 
	 * @param iq
	 * @return
	 */
	private IQ kickGroupMember(IQ iq) {
		//取群id
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
		//不能删除自己
		if (member_jid.equals(from))
			return packetUtil.createErrorIq(iq, GroupError.Condition.bad_request);
		String reason = iq.getChildElement().elementText(TAG.REASON);
		try {
			Group group = groupDbManager.getGroupById(group_id);
			//群不存在
			if (group == null)
				return packetUtil.createErrorIq(iq, GroupError.Condition.group_not_exsist);
			//没有权限
			if (!from.equals(group.getCreator()))	
				return packetUtil.createErrorIq(iq, GroupError.Condition.no_permission);
			//不存在该成员
			if (!groupDbManager.isGroupMember(group_id, member_jid)) 
				return packetUtil.createErrorIq(iq, GroupError.Condition.member_not_exsist);
			//删除成员
			groupDbManager.deleteGroupMember(group_id, member_jid);
			//通知被删除成员
			Message kick_msg = packetUtil.createKickMemberResponseMessage(member_jid, group, reason);
			boolean sent = sendPacketIfOnline(kick_msg, member_jid);
			//未发送，保存到数据库
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(from, member_jid, kick_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//通知群成员更新列表
    		//取成员列表
			List<GroupMember> list = groupDbManager.listGroupMembers(group_id);
			//通知被删除人员
			list.add(new GroupMember(group_id, member_jid));
			//发送删除消息
			sendPacket(list, packetUtil.createMemberOutPresence(member_jid, group));
			//处理完成
			return IQ.createResultIQ(iq);
		} catch (SQLException e) {
			e.printStackTrace();
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
	}
}
