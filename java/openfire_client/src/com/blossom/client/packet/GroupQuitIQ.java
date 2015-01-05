package com.blossom.client.packet;

import org.jivesoftware.smack.packet.IQ;

public class GroupQuitIQ extends GroupIQ {

	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "jabber:iq:register";
	
//	<iq from='user1@352.cn' to='13@group.352.cn' type='set'>
//	<query xmlns='jabber:iq:register'>
//	<quit/>
//	</query>
//	</iq>
	
	public GroupQuitIQ(String group_jid) {
		setType(IQ.Type.SET);
		setTo(group_jid);
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"").append(NAMESPACE)
        	.append("\"><quit/></query>");
        return buf.toString();
	}

}
