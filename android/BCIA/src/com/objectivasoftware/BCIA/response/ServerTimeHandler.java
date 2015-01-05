package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.Meta;
import com.objectivasoftware.BCIA.model.ServerTime;

public class ServerTimeHandler extends DefaultHandler{

	private ServerTime serverTime;
	private Meta meta;
	private Exception exception;
	private String tagName;
	
	public ServerTime getServerTime() {
		return serverTime;
	}

	@Override
	public void startDocument() throws SAXException {
		serverTime = new ServerTime();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		if ("meta".equals(localName)) {
			meta = new Meta();
			serverTime.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			serverTime.setException(exception);
			serverTime.getException().seteCode(attributes.getValue(0));
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data = new String(ch, start, length);
		setElement(tagName, data);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tagName = null;
	}
	
	private void setElement(String tagName, String data) {
		if ("userCode".equals(tagName)) {
			serverTime.getMeta().getUserCode();
		} else if ("userPassword".equals(tagName)) {
			serverTime.getMeta().setPassword(data);
		} else if ("serverTime".equals(tagName)) {
			serverTime.getMeta().setServerTime(data);
		} else if ("servernow".equals(tagName)) {
			serverTime.setServerTime(data);
		} else if ("exception".equals(tagName)) {
			serverTime.getException().setErrMessage(data);
		}
		
	}

}
