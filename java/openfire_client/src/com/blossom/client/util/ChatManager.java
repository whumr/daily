package com.blossom.client.util;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.Packet;

import com.blossom.client.ui.BaseFrame;
import com.blossom.client.ui.ChatFrame;

public class ChatManager {

	Map<String, ChatFrame> map = new HashMap<String, ChatFrame>();
	
	Map<Class<? extends Packet>, BaseFrame> packetMap = new HashMap<Class<? extends Packet>, BaseFrame>();
	
	private static ChatManager chatManager;
	
	private ChatManager() {
	}
	
	public static ChatManager getInstance() {
		if (chatManager == null)
			chatManager = new ChatManager();
		return chatManager;
	}
	
	public void addChat(String jid, ChatFrame frame) {
		map.put(jid, frame);
	}
	
	public ChatFrame getChat(String jid) {
		return map.get(jid);
	}
	
	public void removeChat(String jid) {
		map.remove(jid);
	}
	
	public void setPacketFrame(Class<? extends Packet> packetClass, BaseFrame frame) {
		packetMap.put(packetClass, frame);
	}
	
	public BaseFrame getPacketFrame(Class<? extends Packet> packetClass) {
		return packetMap.get(packetClass);
	}
}
