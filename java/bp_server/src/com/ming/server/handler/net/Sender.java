package com.ming.server.handler.net;

import java.io.IOException;

import com.ming.server.common.domain.Protocol;

public interface Sender {

	public void send(Protocol protocol, String... ips) throws IOException;
	
	public void send(Protocol[] protocols, String ip) throws IOException;
	
	public void send(Protocol protocol) throws IOException;

	public void send(Protocol protocol, String ip) throws IOException;
}
