package com.ming.server.listener;

import java.io.IOException;

public interface MessageListener {

	public void init() throws IOException;

	public void start() throws IOException;
	
	public void destory() throws IOException;
}