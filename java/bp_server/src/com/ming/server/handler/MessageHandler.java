package com.ming.server.handler;

import com.ming.server.common.domain.Protocol;

public interface MessageHandler {

	public static Class<?>[] ARGS = {Protocol.class};
	
	public void handleMessage(Protocol protocol);
}
