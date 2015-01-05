package com.blossom.client.provider.extension;

import java.io.IOException;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.blossom.client.packet.extension.MucUserX;
import com.blossom.client.packet.extension.MucUserX.Decline;
import com.blossom.client.packet.extension.MucUserX.Destroy;
import com.blossom.client.packet.extension.MucUserX.Invite;
import com.blossom.client.packet.extension.MucUserX.Item;
import com.blossom.client.packet.extension.MucUserX.Status;

public class MucUserXProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser)
			throws Exception {
		boolean done = false;
        MucUserX mucUserX = new MucUserX();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
            	String tagName =parser.getName();
                if (tagName.equals("destroy")) { 
                	mucUserX.setDestroy(parseDestroy(parser));
                } else if (tagName.equals("status")) {                    
                	mucUserX.setStatus(parseStatus(parser));
                } else if (tagName.equals("invite")) {                    
                	mucUserX.setInvite(parseInvite(parser));
                } else if (tagName.equals("decline")) {                    
                	mucUserX.setDecline(parseDecline(parser));
                } else if (tagName.equals("item")) {                    
                	mucUserX.addItem(parseItem(parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(mucUserX.getElementName())) {
                    done = true;
                }
            }
        }
        return mucUserX;
	}
	
//	<destroy jid='13@group.352.cn'>
//    <reason><![CDATA[不想要了]]></reason>
//  </destroy>
	private Destroy parseDestroy(XmlPullParser parser) throws XmlPullParserException, IOException {
		boolean done = false;
		Destroy destroy = new Destroy();
		destroy.setJid(parser.getAttributeValue("", "jid"));
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) { 
                	destroy.setReason(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("destroy")) {
                    done = true;
                }
            }
		}
		return destroy;
	}

//	<status code='307'/>
	private Status parseStatus(XmlPullParser parser) {
		Status status = new Status();
		status.setCode(parser.getAttributeValue("", "code"));
		return status;
	}
	
//	<item affiliation='none' jid='user2@352.cn' nick='user2' role='none'/>
	private Item parseItem(XmlPullParser parser) {
		Item item = new Item();
		item.setAffiliation(parser.getAttributeValue("", "affiliation"));
		item.setJid(parser.getAttributeValue("", "jid"));
		item.setNick(parser.getAttributeValue("", "nick"));
		item.setRole(parser.getAttributeValue("", "role"));
		return item;
	}
	
//	<invite to='user2@352.cn'>
//	<reason><![CDATA[我是xx]]></reason>
//	</invite>
//	<invite from='user1@352.cn'>
//	<reason><![CDATA[我是xx]]></reason>
//	</invite>
	private Invite parseInvite(XmlPullParser parser) throws Exception {
		boolean done = false;
		Invite invite = new Invite();
		String from = parser.getAttributeValue("", "from");
		String to = parser.getAttributeValue("", "to");
		if (from != null)
			invite.setFrom(from);
		if (to != null)
			invite.setTo(to);
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) { 
                	invite.setReason(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("invite")) {
                    done = true;
                }
            }
		}
		return invite;
	}
	
//	<decline to='user1@352.cn' result='1\2'>
//	<reason><![CDATA[我不想加入]]></reason>
//	</decline>
//	<decline from='user2@352.cn' result='1\2'>
//	<reason><![CDATA[我不想加入]]></reason>
//	</decline>
	private Decline parseDecline(XmlPullParser parser) throws Exception {
		boolean done = false;
		Decline decline = new Decline();
		String from = parser.getAttributeValue("", "from");
		String to = parser.getAttributeValue("", "to");
		String result = parser.getAttributeValue("", "result");
		if (from != null)
			decline.setFrom(from);
		if (to != null)
			decline.setTo(to);
		if (result != null)
			decline.setResult(result);
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) { 
                	decline.setReason(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("decline")) {
                    done = true;
                }
            }
		}
		return decline;
	}

}