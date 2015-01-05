package com.blossom.client.packet.extension;

import org.jivesoftware.smack.packet.PacketExtension;

public class GroupInformation implements PacketExtension {

	public static final String ELEMENTNAME = "group";
	public static final String NAMESPACE = "http://jabber.org/protocol/muc";
	
	private String to;
	private String id;
	
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
        	.append(getNamespace()).append("\"");
        if (to != null && to.length() > 0)
            buf.append(" to=\"").append(to).append("\"");
        if (id != null && id.length() > 0)
        	buf.append(" id=\"").append(id).append("\"");
        buf.append(" />");
        return buf.toString();
	}
	
	public boolean isSend() {
		return to != null && id == null;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
