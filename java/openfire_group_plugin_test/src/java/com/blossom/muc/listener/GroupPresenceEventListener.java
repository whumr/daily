package com.blossom.muc.listener;

import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.PresenceEventListener;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;

public class GroupPresenceEventListener implements PresenceEventListener {

	@Override
	public void availableSession(ClientSession session, Presence presence) {
		//上线通知群成员，更改群成员状态
	}

	@Override
	public void unavailableSession(ClientSession session, Presence presence) {
		//下线通知群成员，更改群成员状态
	}

	@Override
	public void presenceChanged(ClientSession session, Presence presence) {
		
	}

	@Override
	public void subscribedToPresence(JID subscriberJID, JID authorizerJID) {
	}

	@Override
	public void unsubscribedToPresence(JID unsubscriberJID, JID recipientJID) {
	}

}
