package com.ming.server.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.ming.server.common.domain.Protocol;

public interface ProtocolUtil {

	public Protocol readProtocol(DataInputStream in) throws IOException;
	
	public Protocol readProtocol(DataInputStream in, boolean closeIO) throws IOException;
	
	public void writeProtocol(DataOutputStream out, Protocol protocol) throws IOException;
	
	public void writeProtocol(DataOutputStream out, Protocol protocol, boolean closeIO) throws IOException;
	
	public void setVersion(String version);

	public String getVersion();
}
