package com.blossom.test.component;

import org.jivesoftware.openfire.user.PresenceEventDispatcher;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.blossom.test.component.handler.GroupIqHandler;
import com.blossom.test.component.handler.GroupMessageHandler;
import com.blossom.test.component.handler.GroupPresenceHandler;
import com.blossom.test.component.listener.GroupMemberListener;

public class GroupComponent implements Component {

	private String serviceName;
	private GroupIqHandler iqHandler;
	private GroupPresenceHandler presenceHandler;
	private GroupMessageHandler messageHandler;
	
	private static GroupComponent groupComponent;
	
	private GroupComponent() {
		serviceName = JiveGlobals.getProperty(Constants.COMPONENT_NAME_KEY, Constants.DEFAULT_COMPONENT_NAME);
		iqHandler = new GroupIqHandler();
		presenceHandler = new GroupPresenceHandler();
		messageHandler = new GroupMessageHandler();
	}
	
	public static GroupComponent getInstance() {
		if (groupComponent == null)
			groupComponent = new GroupComponent();
		return groupComponent;
	}
	
	@Override
	public void processPacket(Packet packet) {
		System.out.println("UserGroupComponent received :\t" + packet.toXML());
		if (packet instanceof Presence) {
			Presence presence = (Presence)packet;
			processPresence(presence);
		} else if (packet instanceof Message) {
			Message message = (Message)packet;
			processMessage(message);
		} else if (packet instanceof IQ) {
			IQ iq = (IQ)packet;
			processIq(iq);
		} 
	}
	
	private void processPresence(Presence presence) {
		presenceHandler.processPresence(presence);
	}
	
	private void processMessage(Message message) {
		messageHandler.processMessage(message);
	}

	private void processIq(IQ iq) {
		iqHandler.processIq(iq);
	}
	
	@Override
	public String getName() {
		return serviceName;
	}

	@Override
	public String getDescription() {
		return serviceName;
	}

	@Override
	public void initialize(JID jid, ComponentManager componentManager)
			throws ComponentException {
		//加入用户上线监听器，用来推送群相关信息
		GroupMemberListener groupMemberListener = new GroupMemberListener();
		PresenceEventDispatcher.addListener(groupMemberListener);
	}

	@Override
	public void start() {
	}

	@Override
	public void shutdown() {
	}
}
