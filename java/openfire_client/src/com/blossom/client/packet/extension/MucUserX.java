package com.blossom.client.packet.extension;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.PacketExtension;

public class MucUserX implements PacketExtension {
	
	public static final String ELEMENTNAME = "x";
	public static final String NAMESPACE = "http://jabber.org/protocol/muc#user";

	private Destroy destroy;
	private Status status;
	private Invite invite;
	private Decline decline;
	private List<Item> items = new CopyOnWriteArrayList<Item>();
	
	@Override
	public String getElementName() {
		return ELEMENTNAME;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"")
        	.append(getNamespace()).append("\">");
        if (destroy != null)
        	buf.append(destroy.toXML());
        if (invite != null)
        	buf.append(invite.toXML());
        if (decline != null)
        	buf.append(decline.toXML());
        if (!items.isEmpty()) {
        	for (Item item : items) {
        		buf.append(item.toXML());
			}
        }
        if (status != null)
        	buf.append(status.toXML());
        buf.append("</x>");
        return buf.toString();
	}

	public Destroy getDestroy() {
		return destroy;
	}

	public void setDestroy(Destroy destroy) {
		this.destroy = destroy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Invite getInvite() {
		return invite;
	}

	public void setInvite(Invite invite) {
		this.invite = invite;
	}

	public Decline getDecline() {
		return decline;
	}

	public void setDecline(Decline decline) {
		this.decline = decline;
	}

	public List<Item> getItems() {
		return items;
	}
	
	public Item getItem() {
		return items.isEmpty() ? null : items.get(0);
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

//	<x xmlns='http://jabber.org/protocol/muc#user'>
//  <destroy jid='13@group.352.cn'>
//    <reason><![CDATA[不想要了]]></reason>
//  </destroy>
//</x>
	public static class Destroy {
		private String jid;
		private String reason;

		public String toXML() {
			StringBuilder buf = new StringBuilder();
	        buf.append("<destroy");
	        if (jid != null && jid.length() > 0)
	            buf.append(" jid=\"").append(jid).append("\">");
	        if (reason != null && reason.length() > 0)
	        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
	        buf.append("</destroy>");
	        return buf.toString();
		}

		public String getJid() {
			return jid;
		}

		public void setJid(String jid) {
			this.jid = jid;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}
	
//	<x xmlns='http://jabber.org/protocol/muc#user'>
//	<item affiliation='none' jid='user2@352.cn' nick='user2' role='none'/>
//	<status code='307'/>
//	</x>
	public static class Item {
		private String affiliation;
		private String jid;
		private String nick;
		private String role;
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
	        buf.append("<item ");
	        if (affiliation != null && affiliation.length() > 0)
	            buf.append(" affiliation=\"").append(affiliation).append("\"");
	        if (jid != null && jid.length() > 0)
	        	buf.append(" jid=\"").append(jid).append("\"");
	        if (nick != null && nick.length() > 0)
	        	buf.append(" nick=\"").append(nick).append("\"");
	        if (role != null && role.length() > 0)
	        	buf.append(" role=\"").append(role).append("\"");
	        buf.append(" />");
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
	}
	
	public static class Status {
		
		public static final String NEW_CODE = "301";
		public static final String KICK_CODE = "307";
		public static final String QUIT_CODE = "308";
		
		private String code;
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
	        buf.append("<status ");
	        if (code != null && code.length() > 0)
	            buf.append(" code=\"").append(code).append("\"");
	        buf.append(" />");
	        return buf.toString();
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
	
//	<invite to='user2@352.cn'>
//	<reason><![CDATA[我是xx]]></reason>
//	</invite>
//	<invite from='user1@352.cn'>
//	<reason><![CDATA[我是xx]]></reason>
//	</invite>
	public static class Invite {
		private String from;
		private String to;
		private String reason;
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
	        buf.append("<invite ");
	        if (from != null && from.length() > 0)
	            buf.append(" from=\"").append(from).append("\"");
	        if (to != null && to.length() > 0)
	        	buf.append(" to=\"").append(to).append("\"");
	        buf.append(">");
	        if (reason != null && reason.length() > 0)
	        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
	        buf.append("</invite>");
	        return buf.toString();
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}
	
//	<decline to='user1@352.cn' result='1\2'>
//	<reason><![CDATA[我不想加入]]></reason>
//	</decline>
//	<decline from='user2@352.cn' result='1\2'>
//	<reason><![CDATA[我不想加入]]></reason>
//	</decline>
	public static class Decline {
		private String from;
		private String to;
		private String reason;
		private String result;

		public static final String RESULT_ACCEPT = "1";
		public static final String RESULT_DENIED = "2";
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
	        buf.append("<decline ");
	        if (from != null && from.length() > 0)
	            buf.append(" from=\"").append(from).append("\"");
	        if (to != null && to.length() > 0)
	        	buf.append(" to=\"").append(to).append("\"");
	        if (result != null && result.length() > 0)
	        	buf.append(" result=\"").append(result).append("\"");
	        buf.append(">");
	        if (reason != null && reason.length() > 0)
	        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
	        buf.append("</decline>");
	        return buf.toString();
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}
}
