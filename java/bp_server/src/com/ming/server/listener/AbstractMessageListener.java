package com.ming.server.listener;

import java.io.IOException;

public abstract class AbstractMessageListener implements MessageListener {
	
	protected boolean inited = false;

	public abstract void init() throws IOException;

	public abstract void start() throws IOException;

	public abstract void destory() throws IOException;

}