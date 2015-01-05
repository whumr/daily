package com.blossom.client.ui;

import org.jivesoftware.smack.packet.Message;

public abstract class ChatFrame extends BaseFrame {

	private static final long serialVersionUID = -5945274034335946554L;

	public abstract void messageReceived(Message msg);

}
