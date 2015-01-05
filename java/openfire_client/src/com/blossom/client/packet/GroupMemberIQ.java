package com.blossom.client.packet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

public class GroupMemberIQ extends GroupIQ {
	
	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "http://jabber.org/protocol/muc#admin";
	
	private final List<Item> items = new CopyOnWriteArrayList<Item>();
	private String reason;
//
//	<iq from='user1@352.cn' to='13@group.352.cn' type='get'>
//	<query xmlns='http://jabber.org/protocol/muc#admin'>
//	<item affiliation='member'/>
//	</query>
//	</iq>
//
//	<iq from='13@group.352.cn' to='user1@352.cn' type='result'>
//	  <query xmlns='http://jabber.org/protocol/muc#admin'>
//	     <item affiliation='member' jid='user1@352.cn' nick='user1' role='owner' online='true'/>
//	     <item affiliation='member' jid='user2@352.cn' nick='user2' role='admin' online='false'/>
//	  </query>
//	</iq>

	public static GroupMemberIQ createMemberQueryIq(String from, String to) {
		GroupMemberIQ iq = new GroupMemberIQ();
		iq.setFrom(from);
		iq.setTo(to);
		iq.setType(IQ.Type.GET);
		iq.addItem(new Item());
		return iq;
	}
	
//	<iq from='user1@352.cn' to='13@group.352.cn' type='set'>
//	  <query xmlns='http://jabber.org/protocol/muc#admin'>
//	    <item jid='user2@352.cn'>
//	      <reason><![CDATA[不想要了]]></reason>
//	    </item>
//	  </query>
//	</iq>
	public static GroupMemberIQ createKickMemberIq(String jid, String group_jid, String reason) {
		GroupMemberIQ iq = new GroupMemberIQ();
		iq.setType(IQ.Type.SET);
		iq.setTo(group_jid);
		Item item = new Item();
		item.setJid(jid);
		item.setRole("none");
		iq.addItem(item);
		iq.setReason(reason);
		return iq;
	}
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"" + NAMESPACE + "\">");
        for (Item item : items) {
            buf.append(item.toXML());
        }
        if (reason != null)
        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
        buf.append("</query>");
        return buf.toString();
	}

	public List<Item> getItems() {
		return items;
	}
	
	public void addItem(Item e) {
		items.add(e);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static class Item {
		private String affiliation;
		private String jid;
		private String nick;
		private String role;
		private String online;
		
		public Item() {
			this("member");
		}

		public Item(String affiliation) {
			this.affiliation = affiliation;
		}
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
            buf.append("<item affiliation=\"").append(affiliation).append("\"");
            if (jid != null) {
                buf.append(" jid=\"").append(StringUtils.escapeForXML(jid)).append("\"");
            }
            if (nick != null) {
                buf.append(" nick=\"").append(StringUtils.escapeForXML(nick)).append("\"");
            }
            if (role != null) {
                buf.append(" role=\"").append(StringUtils.escapeForXML(role)).append("\"");
            }
            if (online != null) {
            	buf.append(" online=\"").append(StringUtils.escapeForXML(online)).append("\"");
            }
            buf.append("/>");
            return buf.toString();
		}
		
		public String getAffiliation() {
			return affiliation;
		}
		public void setAffiliation(String affiliation) {
			this.affiliation = affiliation;
		}
		public String getJid() {
			return jid;
		}
		public void setJid(String jid) {
			this.jid = jid;
		}
		public String getNick() {
			return nick;
		}
		public void setNick(String nick) {
			this.nick = nick;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getOnline() {
			return online;
		}
		public void setOnline(String online) {
			this.online = online;
		}
	}
}
