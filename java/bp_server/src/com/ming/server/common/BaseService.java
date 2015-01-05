package com.ming.server.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

import com.ming.server.common.domain.Protocol;

public interface BaseService {

	public void handleProtocol(Protocol protocol, Socket socket) throws IOException;

	public void handleProtocol(Protocol protocol, DatagramPacket packet) throws IOException;
}