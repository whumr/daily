package com.blossom.muc;

import org.dom4j.DocumentHelper;
import org.dom4j.QName;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.spi.LocalMUCRole;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.jivesoftware.openfire.muc.spi.LocalMUCUser;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

public class LocalGroupRole extends LocalMUCRole {

	public LocalGroupRole(LocalGroupUser chatuser, Affiliation affiliation, String nick) {
		this.bareJID = chatuser.getAddress();
        this.affiliation = affiliation;
        this.nick = nick;
	}
	
	public LocalGroupRole(GroupServiceImpl chatserver, LocalGroup chatroom, LocalGroupUser chatuser, Role role,
			Affiliation affiliation) {
		this.room = chatroom;
        this.nick = chatuser.getAddress().getNode();
        this.bareJID = chatuser.getAddress();
        this.server = chatserver;
        this.role = role;
        this.affiliation = affiliation;
        // Cache the user's session (will only work for local users)
        extendedInformation = DocumentHelper.createElement(QName.get("x", "http://jabber.org/protocol/muc#user"));
        calculateExtendedInformation();
	}
	
	public LocalGroupRole(MultiUserChatService chatserver,
			LocalMUCRoom chatroom, String nickname, Role role,
			Affiliation affiliation, LocalMUCUser chatuser, Presence presence,
			PacketRouter packetRouter) {
		super(chatserver, chatroom, nickname, role, affiliation, chatuser, presence,
				packetRouter);
	}

	@Override
	public void changeNickname(String nickname) {
		// FIXME Auto-generated method stub
		super.changeNickname(nickname);
	}
	
	@Override
	public MUCRoom getChatRoom() {
		// FIXME Auto-generated method stub
		return super.getChatRoom();
	}
	
	@Override
	public void send(Packet packet) {
		// FIXME Auto-generated method stub
		super.send(packet);
	}
}