package com.blossom.client.packet;

import org.jivesoftware.smack.packet.IQ;

public abstract class GroupIQ extends IQ {

	public abstract String getChildElementXML();

}
