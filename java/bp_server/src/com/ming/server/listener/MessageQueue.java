package com.ming.server.listener;

import java.util.List;
import java.util.Vector;

import com.ming.server.common.domain.Protocol;

public class MessageQueue {

	private static List<Protocol> protocolList;
	
	public static void putMessage(Protocol protocol) {
		if(protocolList == null)
			protocolList = new Vector<Protocol>();
		protocolList.add(protocol);
	}
	
	public static Protocol getMessage() {
		if(protocolList == null || protocolList.isEmpty())
			return null;
		Protocol msg = protocolList.get(0);
		protocolList.remove(0);
		return msg;
	}
}