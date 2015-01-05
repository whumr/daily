package com.ming.server.common.domain;

public class Response extends Protocol {

	private static final long serialVersionUID = -728988001585669218L;
	
	protected int opCode;
	
	protected int responseCode;
	
	protected int contentLength;
	
	protected String content;
	
	public int getOpCode() {
		return opCode;
	}

	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}