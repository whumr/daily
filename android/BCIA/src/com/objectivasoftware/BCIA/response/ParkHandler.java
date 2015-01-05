package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.Meta;
import com.objectivasoftware.BCIA.model.Park;
import com.objectivasoftware.BCIA.model.Parks;

public class ParkHandler extends DefaultHandler {

	private Parks parks;
	public Parks getParks() {
		return parks;
	}

	private String tagName;
	private Park park;
	private Meta meta;
	private Exception exception;
	
	@Override
	public void startDocument() throws SAXException {
		parks = new Parks();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		if ("park".equals(localName)) {
			park = new Park();
			park.setSelect(Boolean.valueOf(attributes.getValue(0)));
		} else if ("meta".equals(localName)) {
			meta = new Meta();
			parks.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			parks.setException(exception);
			parks.getException().seteCode(attributes.getValue(0));
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
		if ("park".equals(localName)) {
			parks.addParkList(park);
		}
		
		tagName = null;
	}
	
	private void setElement(String tagName, String data) {
		if ("userCode".equals(tagName)) {
			parks.getMeta().setUserCode(data);
		} else if ("userPassword".equals(tagName)) {
			parks.getMeta().setPassword(data);
		} else if ("serverTime".equals(tagName)) {
			parks.getMeta().setServerTime(data);
		} else if ("park".equals(tagName)) {
			park.setParkValue(data);
		} else if ("exception".equals(tagName)) {
			parks.getException().setErrMessage(data);
		}
		
	}

}
