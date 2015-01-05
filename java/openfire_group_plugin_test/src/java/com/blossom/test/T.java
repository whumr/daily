package com.blossom.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.util.LocaleUtils;
import org.xmpp.forms.DataForm;
import org.xmpp.forms.FormField;
import org.xmpp.forms.FormField.Type;

public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Element element = DocumentHelper.createElement(QName.get("query",
                "http://jabber.org/protocol/muc#owner"));

		DataForm configurationForm = new DataForm(DataForm.Type.form);
//        configurationForm.setTitle(LocaleUtils.getLocalizedString("muc.form.conf.title"));
        configurationForm.setTitle("test title");
        List<String> params = new ArrayList<String>();
        params.add("xx");
//        configurationForm.addInstruction(LocaleUtils.getLocalizedString("muc.form.conf.instruction", params));
        configurationForm.addInstruction("muc.form.conf.instruction");

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
        
        System.out.println(configurationForm.getElement().asXML());
	}

}
