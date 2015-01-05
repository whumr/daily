package com.blossom.muc.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.util.LocaleUtils;
import org.xmpp.forms.DataForm;
import org.xmpp.forms.FormField;
import org.xmpp.forms.FormField.Type;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

import com.blossom.muc.GroupServiceImpl;
import com.blossom.muc.LocalGroup;

public class GroupIQOwnerHandler extends BaseHandler {

	public GroupIQOwnerHandler(GroupServiceImpl service) {
		super(service);
	}

	public IQ handleIQ(IQ packet) {
		Element element = packet.getChildElement();
		Element xElement = element.element(QName.get("x", "jabber:x:data"));
		Element destroyElement = element.element("destroy");
		//新建群配置群
        if (xElement != null) {
        	return configGroup(packet);
        //删除群
        } else if (destroyElement != null) {
            return deleteGroup(packet);
        //请求配置
        } else {
        	IQ reply = IQ.createResultIQ(packet);
        	reply.setChildElement(getConfigElement(null));
        	return reply;
        }
	}
        
//	/**
//	 * 创建群
//	 * 
//	 * @param iq
//	 * @return
//	 */
//	@SuppressWarnings("unused")
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
	 * 配置群
	 * 
	 * @param iq
	 * @return
	 */
	private IQ configGroup(IQ iq) {
		IQ reply = IQ.createResultIQ(iq);
		LocalGroup localGroup = service.getGroup(Long.parseLong(iq.getTo().getNode()));
		if (localGroup != null) {
			DataForm completedForm = new DataForm(iq.getChildElement().element(QName.get("x", "jabber:x:data")));
			//读取提交的配置
			FormField field = completedForm.getField("muc#roomconfig_roomname");
	        if (field != null) {
	            final String value = field.getFirstValue();
	            localGroup.setNaturalLanguageName((value != null ? value : " "));
	        }
	        
	        field = completedForm.getField("muc#roomconfig_roomdesc");
	        if (field != null) {
	            final String value = field.getFirstValue();
	            localGroup.setDescription((value != null ? value : " "));
	        }
	        
	        field = completedForm.getField("muc#roomconfig_persistentroom");
	        if (field != null) {
	            final String value = field.getFirstValue();
	            String booleanValue = ((value != null ? value : "1"));
	            boolean isPersistent = ("1".equals(booleanValue));
	            localGroup.setPersistent(isPersistent);
	        }
	        
	        // Get the new list of owners
	        field = completedForm.getField("muc#roomconfig_roomowners");
	        List<JID> owners = new ArrayList<JID>(); 
	        if (field != null) {
	        	for(String value : field.getValues()) {
	        		// XEP-0045: "Affiliations are granted, revoked, and 
	        		// maintained based on the user's bare JID, (...)"
	                if (value != null && value.trim().length() != 0) {
	        		    owners.add(new JID(value.trim()).asBareJID());
	                }
	        	}
	        }
	        
	        //更新romm状态isdelete至为0
	        try {
				groupDbManager.updateGroupIsdelete(localGroup);
			} catch (SQLException e) {
				reply.setError(PacketError.Condition.internal_server_error);
			}
		}
		return reply;
	}
	
	/**
	 * 删除群
	 * 
	 * @param iq
	 * @return
	 */
	private IQ deleteGroup(IQ iq) {
		Element destroyElement = iq.getChildElement().element("destroy");
		String group = destroyElement.attributeValue("jid");
		if (group != null) {
			String reason = destroyElement.elementTextTrim("reason");
			IQ reply = IQ.createResultIQ(iq);
			long group_id = -1;
			LocalGroup localGroup = null;
			try {
				group_id = Long.parseLong(new JID(group).getNode());
				localGroup = service.getGroup(group_id);
			} catch (Exception e) {
				reply.setError(PacketError.Condition.bad_request);
				return reply;
			}
			try {
				//删除群
				groupDbManager.deleteGroupById(group_id);
				//发送删除消息
				sendPacket(localGroup.getGroupMembers().keySet(), packetUtil.createGroupDeletePresence(group_id, reason));
				//通知cluster
//				service.getGroup(group_id).destroyRoom(new JID(group), reason);
				//反馈删除结果
				return reply;
			} catch (SQLException e) {
				reply.setError(PacketError.Condition.internal_server_error);
				return reply;
			}
		}
		return null;
	}
	
	/**
	 * 取群配置表单
	 * 
	 * @param name
	 * @return
	 */
	private Element getConfigElement(String name) {
		Element element = DocumentHelper.createElement(QName.get("query",
                "http://jabber.org/protocol/muc#owner"));

		DataForm configurationForm = new DataForm(DataForm.Type.form);
        configurationForm.setTitle(LocaleUtils.getLocalizedString("muc.form.conf.title"));
        List<String> params = new ArrayList<String>();
        params.add(name);
        configurationForm.addInstruction(LocaleUtils.getLocalizedString("muc.form.conf.instruction", params));

        configurationForm.addField("FORM_TYPE", null, Type.hidden)
				.addValue("http://jabber.org/protocol/muc#roomconfig");

        configurationForm.addField("muc#roomconfig_roomname", 
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_roomname"),
				Type.text_single);

        configurationForm.addField("muc#roomconfig_roomdesc",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_roomdesc"),
        		Type.text_single);

        configurationForm.addField("muc#roomconfig_changesubject",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_changesubject"),
        		Type.boolean_type);
        
        final FormField maxUsers = configurationForm.addField(
        		"muc#roomconfig_maxusers",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_maxusers"),
        		Type.list_single);
        maxUsers.addOption("10", "10");
        maxUsers.addOption("20", "20");
        maxUsers.addOption("30", "30");
        maxUsers.addOption("40", "40");
        maxUsers.addOption("50", "50");
        maxUsers.addOption(LocaleUtils.getLocalizedString("muc.form.conf.none"), "0");

        final FormField broadcast = configurationForm.addField(
        		"muc#roomconfig_presencebroadcast",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_presencebroadcast"),
        		Type.list_multi);
        broadcast.addOption(LocaleUtils.getLocalizedString("muc.form.conf.moderator"), "moderator");
        broadcast.addOption(LocaleUtils.getLocalizedString("muc.form.conf.participant"), "participant");
        broadcast.addOption(LocaleUtils.getLocalizedString("muc.form.conf.visitor"), "visitor");

        configurationForm.addField("muc#roomconfig_publicroom", 
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_publicroom"),
        		Type.boolean_type);

        configurationForm.addField("muc#roomconfig_persistentroom",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_persistentroom"),
        		Type.boolean_type);

        configurationForm.addField("muc#roomconfig_moderatedroom",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_moderatedroom"),
        		Type.boolean_type);

        configurationForm.addField("muc#roomconfig_membersonly",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_membersonly"),
        		Type.boolean_type);

        configurationForm.addField(null, null, Type.fixed)
        		.addValue(LocaleUtils.getLocalizedString("muc.form.conf.allowinvitesfixed"));

        configurationForm.addField("muc#roomconfig_allowinvites",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_allowinvites"),
        		Type.boolean_type);

        configurationForm.addField("muc#roomconfig_passwordprotectedroom",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_passwordprotectedroom"),
        		Type.boolean_type);

        configurationForm.addField(null, null, Type.fixed)
        		.addValue(LocaleUtils.getLocalizedString("muc.form.conf.roomsecretfixed"));

        configurationForm.addField("muc#roomconfig_roomsecret",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_roomsecret"),
        		Type.text_private);
        
        final FormField whois = configurationForm.addField(
        		"muc#roomconfig_whois",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_whois"),
        		Type.list_single);
        whois.addOption(LocaleUtils.getLocalizedString("muc.form.conf.moderator"), "moderators");
        whois.addOption(LocaleUtils.getLocalizedString("muc.form.conf.anyone"), "anyone");

        configurationForm.addField("muc#roomconfig_enablelogging",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_enablelogging"),
        		Type.boolean_type);

        configurationForm.addField("x-muc#roomconfig_reservednick",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_reservednick"),
        		Type.boolean_type);

        configurationForm.addField("x-muc#roomconfig_canchangenick",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_canchangenick"),
        		Type.boolean_type);

        configurationForm.addField(null, null, Type.fixed)
                .addValue(LocaleUtils.getLocalizedString("muc.form.conf.owner_registration"));

        configurationForm.addField("x-muc#roomconfig_registration",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_registration"),
        		Type.boolean_type);

        configurationForm.addField(null, null, Type.fixed)
                .addValue(LocaleUtils.getLocalizedString("muc.form.conf.roomadminsfixed"));

        configurationForm.addField("muc#roomconfig_roomadmins",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_roomadmins"),
        		Type.jid_multi);

        configurationForm.addField(null, null, Type.fixed)
				.addValue(LocaleUtils.getLocalizedString("muc.form.conf.roomownersfixed"));

        configurationForm.addField("muc#roomconfig_roomowners",
        		LocaleUtils.getLocalizedString("muc.form.conf.owner_roomowners"),
        		Type.jid_multi);
        
        element.add(configurationForm.getElement());
		return element;
	}
}
