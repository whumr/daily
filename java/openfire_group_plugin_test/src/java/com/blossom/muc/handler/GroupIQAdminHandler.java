package com.blossom.muc.handler;

import java.sql.SQLException;
import java.util.Map;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketError;

import com.blossom.muc.GroupServiceImpl;
import com.blossom.muc.LocalGroup;
import com.blossom.muc.LocalGroupRole;
import com.blossom.test.entity.GroupSysMessage;

public class GroupIQAdminHandler extends BaseHandler {

	public GroupIQAdminHandler(GroupServiceImpl service) {
		super(service);
	}

	public IQ handleIQ(IQ iq) {
		Element element = iq.getChildElement();
		Element itemElement = element.element("item");
		if (itemElement != null) {
			Type type = iq.getType();
			if (Type.get == type) {
				return handleIQGet(iq, itemElement);
			} else if (Type.set == type) {
				return handleIQSet(iq, itemElement);
			}
			return null;
		} else {
			IQ reply = IQ.createResultIQ(iq);
			reply.setChildElement(iq.getChildElement().createCopy());
            reply.setError(PacketError.Condition.bad_request);
            return reply;
		}
	}
	
	private IQ handleIQGet(IQ iq, Element item) {
		String affiliation = item.attributeValue("affiliation");
		String group = iq.getTo().getNode();
		if ("member".equals(affiliation)) {
			IQ reply = IQ.createResultIQ(iq);
			LocalGroup localGroup = null;
			try {
				localGroup = service.getGroup(Long.parseLong(group));
			} catch (Exception e) {
				reply.setError(PacketError.Condition.bad_request);
				return reply;
			}
			//Ⱥ��Ա�л�ȡ��Ա�б��Ȩ��
			if (localGroup == null) {
				reply.setError(PacketError.Condition.item_not_found);
			} else if (localGroup.isMember(iq.getFrom().toBareJID())) {
				Map<String, LocalGroupRole> groupMembers = localGroup.getGroupMembers();
				Element result = reply.setChildElement("query", "http://jabber.org/protocol/muc#admin");
				for (LocalGroupRole member : groupMembers.values()) {
					Element metaData = result.addElement("item");
					metaData.addAttribute("affiliation", "member");
                    metaData.addAttribute("jid", member.getUserAddress().toBareJID());
                    metaData.addAttribute("role", member.getAffiliation().toString());
                    metaData.addAttribute("nick", member.getNickname());
                    metaData.addAttribute("online", String.valueOf(isUserOnline(member.getUserAddress().toBareJID())));
				}
			} else {
				reply.setError(PacketError.Condition.forbidden);
			}
			return reply;
		}
		return null;
	}

	private IQ handleIQSet(IQ iq, Element item) {
		String jid = item.attributeValue("jid");
		String role = item.attributeValue("role");
		String group = iq.getTo().getNode();
		IQ reply = IQ.createResultIQ(iq);
		if ("none".equals(role) && jid != null && !"".equals(jid.trim())) {
			LocalGroup localGroup = null;
			try {
				localGroup = service.getGroup(Long.parseLong(group));
			} catch (Exception e) {
				reply.setError(PacketError.Condition.bad_request);
				return reply;
			}
			String reason = item.elementTextTrim("reason");
			String from = iq.getFrom().toBareJID();
			//����ɾ���Լ�
			if (jid.equals(from) || !localGroup.isOwner(from) || !localGroup.isMember(jid)) {
				reply.setError(PacketError.Condition.forbidden);
				return reply;
			}
			try {
				long group_id = Long.parseLong(group);
				//ɾ����Ա
				groupDbManager.deleteGroupMember(group_id, jid);
				//֪ͨ��ɾ����Ա
				Message kick_msg = packetUtil.createKickMemberResponseMessage(jid, localGroup.getGroup(), reason);
				boolean sent = sendPacketIfOnline(kick_msg, jid);
				//δ���ͣ����浽���ݿ�
				if (!sent) {
					GroupSysMessage sysMessage = new GroupSysMessage(from, jid, kick_msg.toXML());
					groupDbManager.insertGroupSysMessage(sysMessage);
				}
				//֪ͨȺ��Ա�����б�
				//����ɾ����Ϣ
				sendPacket(localGroup.getGroupMembers().keySet(), packetUtil.createMemberOutPresence(jid, localGroup.getGroup()));
				//�����ڴ�
				localGroup.removeMember(jid);
				//�������
				return IQ.createResultIQ(iq);
			} catch (SQLException e) {
				reply.setError(PacketError.Condition.internal_server_error);
			}
		} else {
			reply.setError(PacketError.Condition.bad_request);
		}
		return reply;
	}
}