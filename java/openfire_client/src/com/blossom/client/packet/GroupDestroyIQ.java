package com.blossom.client.packet;

import org.jivesoftware.smack.packet.IQ;

import com.blossom.client.util.Constants;

public class GroupDestroyIQ extends GroupIQ {
	
	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "http://jabber.org/protocol/muc#owner";
//	<iq from='user1@352.cn' to='group.352.cn' type='set'>
//	  <query xmlns='http://jabber.org/protocol/muc#owner'>
//	    <destroy jid='13@group.352.cn'>
//	      <reason><![CDATA[不想要了]]></reason>
//	    </destroy>
//	  </query>
//	</iq>

	private String jid;
	private String reason;
	
	public GroupDestroyIQ() {
		setType(IQ.Type.SET);
		setTo(Constants.SERVER_NAME);
	}
	
	public GroupDestroyIQ(String jid) {
		this();
		this.jid = jid;
	}

	public GroupDestroyIQ(String jid, String reason) {
		this();
		this.jid = jid;
		this.reason = reason;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"").append(NAMESPACE)
        	.append("\"><destroy jid=\"").append(jid).append("\">");
        if (reason != null)
        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
        buf.append("</destroy></query>");
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