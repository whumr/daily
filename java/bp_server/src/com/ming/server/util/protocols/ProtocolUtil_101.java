package com.ming.server.util.protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.ming.server.common.domain.Protocol;

public class ProtocolUtil_101 extends ProtocolUtilSupport {

	@Override
	public Protocol readProtocol(DataInputStream in) throws IOException {
		// TODO Auto-generated method stub
		return super.readProtocol(in);
	}

	@Override
	public Protocol readProtocol(DataInputStream in, boolean closeIO)
			throws IOException {
		// TODO Auto-generated method stub
		return super.readProtocol(in, closeIO);
	}

	@Override
	public void writeProtocol(DataOutputStream out, Protocol protocol)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeProtocol(DataOutputStream out, Protocol protocol,
			boolean closeIO) throws IOException {
		// TODO Auto-generated method stub
		super.writeProtocol(out, protocol, closeIO);
	}

	public Protocol createResponse(Protocol protocol) {
		// TODO Auto-generated method stub
		
		return null;
	}
}
