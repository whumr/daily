package com.ming.server.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

import com.ming.server.common.domain.Protocol;

public class ServiceTemplate implements BaseService {

	@Override
	public void handleProtocol(Protocol protocol, Socket socket)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleProtocol(Protocol protocol, DatagramPacket packet)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
