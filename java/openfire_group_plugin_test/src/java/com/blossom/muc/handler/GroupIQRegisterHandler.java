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
		//申请入群
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
			String reason = iq.getChildElement().elementText("reason");
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
	 * 退出群
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
		//删除群成员
		try {
			Map<String, LocalGroupRole> members = localGroup.getGroupMembers();
			if (members != null && !members.isEmpty()) {
				int delete = groupDbManager.deleteGroupMember(group_id, member_jid);
				//有记录被删除,通知群成员
				if (delete > 0) {
					//如果群主退出，删除群
					if (localGroup.isOwner(member_jid))
						groupDbManager.deleteGroupById(group_id);
					//发送消息
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
	 * 审批入群申请
	 * 
	 * @param iq
	 * @return
	 */
	private IQ processGroupApply(IQ iq) {
		IQ reply = IQ.createResultIQ(iq);
		//发送者
		String creator = iq.getFrom().toBareJID();
		Element query = iq.getChildElement();
		//申请人
		String applier = query.elementText("applier");
		//审批结果
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
			//检查是否已经处理过这条请求
			GroupApply apply = groupDbManager.getGroupApplyByUserGroupId(group_id, applier);
			if (apply == null && !localGroup.isOwner(creator)) {
				reply.setError(PacketError.Condition.bad_request);
				return reply;
			}
			//处理请求
			apply.setStatus(result);
			groupDbManager.processGroupApply(apply);
			//如果申请人在线，发送消息通知
			Message applier_msg = packetUtil.createGroupApplyResponseMessage(apply, localGroup.getGroup());
			boolean sent = sendPacketIfOnline(applier_msg, apply.getApply_user());
			//未发送，保存到数据库
			if (!sent) {
				GroupSysMessage sysMessage = new GroupSysMessage(apply.getApply_user(), applier_msg.toXML());
				groupDbManager.insertGroupSysMessage(sysMessage);
			}
			//如果是同意,发送presence给群成员
            if (GroupApply.STATUS_ACCEPT.equals(apply.getStatus())) {
    			//发送消息
    			sendPacket(localGroup.getGroupMembers().keySet(), packetUtil.createNewMemberPresence(apply));
            }			
			//处理完成
		} catch (SQLException e) {
			reply.setError(PacketError.Condition.internal_server_error);
		}
		return reply;
	}
}