package com.ming.server.common.domain;

import java.io.Serializable;
import java.net.Socket;

public class Protocol implements Serializable {

	private static final long serialVersionUID = 5274222727956340748L;

	protected byte type;
	
	protected int version;
	
	protected String[][] fieldOrder;
	
	protected int[] fieldType;

	protected Socket socket;
	
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	
	public boolean isRequest() {
		return type == Constants.REQUEST_CODE;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String[][] getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(String[][] fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public int[] getFieldType() {
		return fieldType;
	}

	public void setFieldType(int[] fieldType) {
		this.fieldType = fieldType;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
