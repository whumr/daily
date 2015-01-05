package com.blossom.client.packet;

import org.jivesoftware.smack.packet.IQ;

import com.blossom.client.util.Constants;

public class GroupApplyIQ extends GroupIQ {
	
	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "jabber:iq:register";
	public static final String RESULT_ACCEPT = "1";
	public static final String RESULT_DENIED = "2";

//	Applier Request£º
//	<iq from='user1@352.cn' to='13@group.352.cn' type='get'>
//	<query xmlns='jabber:iq:register'>
//	<reason><![CDATA[ÎÒÊÇxx]]></reason>
//	</query>
//	</iq>
//	
//	Admin to server£º
//	<iq type='set' from='user2@352.cn' to='13@group.352.cn'>
//	<query xmlns='jabber:iq:register'/>
//	   <applier>user1@352.cn</applier>
//	   <result>1\2</result>
//	</query>
//	</iq>

	private String group_id;
	private String applier;
	private String reason;
	private String result;
	
	public static GroupApplyIQ createApplyIq(String group_id, String reason) {
		GroupApplyIQ iq = new GroupApplyIQ();
		iq.setType(IQ.Type.GET);
		iq.setReason(reason);
		iq.setGroup_id(group_id);
		iq.setTo(Constants.getGroupJid(group_id));
		return iq;
	}

	public static GroupApplyIQ createResultIq(String group_id, String applier, String result) {
		GroupApplyIQ iq = new GroupApplyIQ();
		iq.setType(IQ.Type.SET);
		iq.setGroup_id(group_id);
		iq.setTo(Constants.getGroupJid(group_id));
		iq.setApplier(applier);
		iq.setResult(result);
		return iq;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"" + NAMESPACE + "\">");
        if (reason != null)
        	buf.append("<reason><![CDATA[").append(reason).append("]]></reason>");
        if (applier != null)
        	buf.append("<applier>").append(applier).append("</applier>");
        if (result != null)
        	buf.append("<result>").append(result).append("</result>");
        buf.append("</query>");
        return buf.toString();
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
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
