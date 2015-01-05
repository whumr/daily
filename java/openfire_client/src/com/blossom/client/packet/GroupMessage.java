package com.blossom.client.packet;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

import com.blossom.client.packet.extension.GroupInformation;
import com.blossom.client.packet.extension.MucUserX;

public class GroupMessage extends Message {

	private String newName;
	private MucUserX x;
	
	public GroupMessage() {
		setType(Message.Type.groupchat);
	}
	
//	<message from='user1@352.cn' to='13@group.352.cn' type='chat'>
//    <group xmlns='http://jabber.org/protocol/muc' to='user2@352.cn' />
//    <body><![CDATA[hello world.]]</body>
//	</message>
	public static GroupMessage createPrivateMessage(String group_jid, String to, String body) {
		GroupMessage msg = new GroupMessage();
		msg.setType(Message.Type.chat);
		msg.setTo(group_jid);
		msg.setBody(body);
		GroupInformation gf = new GroupInformation();
		gf.setTo(to);
		msg.addExtension(gf);
		return msg;
	}
	
//	<message from='user1@352.cn' to='13@group.352.cn' type='groupchat'>
//	  <subject>新群名称</subject>
//	</message>
	public static GroupMessage createRenameMessage(String newName, String group_jid) {
		GroupMessage msg = new GroupMessage();
		msg.setTo(group_jid);
		msg.setNewName(newName);
		return msg;
	}
	
//	<message from='user1@352.cn' to='13@group.352.cn'>
//	<x xmlns='http://jabber.org/protocol/muc#user'>
//	<invite to='user2@352.cn'>
//	<reason><![CDATA[我是xx]]></reason>
//	</invite>
//	</x>
//	</message>
	public static GroupMessage createInviteMessage(String group_jid, String invitee_jid, String reason) {
		GroupMessage msg = new GroupMessage();
		msg.setTo(group_jid);
		MucUserX x = new MucUserX();
		MucUserX.Invite invite = new MucUserX.Invite();
		x.setInvite(invite);
		invite.setTo(invitee_jid);
		invite.setReason(reason);
		msg.setX(x);
		return msg;
	}
	
//	<message from='user2@352.cn' to='13@group.352.cn'>
//	<x xmlns='http://jabber.org/protocol/muc#user'>
//	<decline to='user1@352.cn' result='1\2'>
//	<reason><![CDATA[我不想加入]]></reason>
//	</decline>
//	</x>
//	</message>
	public static GroupMessage createInviteReplyMessage(String group_jid, String inviter_jid, String result, String reason) {
		GroupMessage msg = new GroupMessage();
		msg.setTo(group_jid);
		MucUserX x = new MucUserX();
		MucUserX.Decline decline = new MucUserX.Decline();
		x.setDecline(decline);
		decline.setTo(inviter_jid);
		decline.setResult(result);
		decline.setReason(reason);
		msg.setX(x);
		return msg;
	}
	
	public String toXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<message");
		if (getPacketID() != null)
			buf.append(" id=\"").append(getPacketID()).append("\"");
		if (getTo() != null)
			buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
		if (getFrom() != null)
			buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
		if (getType() != Type.normal)
			buf.append(" type=\"").append(getType()).append("\"");
		buf.append(">");
		if (getBody() != null)
			buf.append("<body><![CDATA[").append(getBody()).append("]]></body>");
		//重命名
		if (newName != null)
			buf.append("<subject><![CDATA[").append(newName).append("]]></subject>");
		// Add packet extensions, if any are defined.
		buf.append(getExtensionsXML());
		buf.append("</message>");
		return buf.toString();
	}
	
	public MucUserX getX() {
		return x;
	}
	public void setX(MucUserX x) {
		this.x = x;
		addExtension(x);
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
}
