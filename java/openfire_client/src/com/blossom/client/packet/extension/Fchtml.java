package com.blossom.client.packet.extension;

import org.jivesoftware.smack.packet.PacketExtension;

public class Fchtml implements PacketExtension {

	private String msg;
	
	public Fchtml(String msg) {
		this.msg = msg;
	}

	@Override
	public String getElementName() {
		return "fchtml";
	}

	@Override
	public String getNamespace() {
		return "jabber:client";
	}

	@Override
	public String toXML() {
		return "<fchtml>" + msg + "</fchtml>";
	}

	public String getMsg() {
		return msg;
	}

}
