package com.blossom.client.packet;

import org.jivesoftware.smack.packet.IQ;

import com.blossom.client.util.Constants;

public class GroupNewIQ extends GroupIQ {
	
	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "http://jabber.org/protocol/muc#owner";

//	<iq from='user1@352.cn' to='group.352.cn' type='set'>
//	  <query xmlns='http://jabber.org/protocol/muc#owner'>
//	    <x xmlns='jabber:x:data' type='submit'>≤‚ ‘»∫</x>
//	  </query>
//	</iq>

	private String name;
	
	public GroupNewIQ(String name) {
		setTo(Constants.SERVER_NAME);
		this.name = name;
		setType(IQ.Type.SET);
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"" + NAMESPACE + "\">");
        buf.append("<x xmlns=\"jabber:x:data\" type=\"submit\"><![CDATA[")
        	.append(name).append("]]></x>");
        buf.append("</query>");
        return buf.toString();
	}
}