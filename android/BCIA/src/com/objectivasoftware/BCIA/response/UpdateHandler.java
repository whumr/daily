package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.Meta;
import com.objectivasoftware.BCIA.model.Result;

public class UpdateHandler extends DefaultHandler{

	private Result result;
	public Result getResult() {
		return result;
	}

	private Meta meta;
	private Exception exception;
	private String tagName;
	
	@Override
	public void startDocument() throws SAXException {
		result = new Result();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		if ("meta".equals(localName)) {
			meta = new Meta();
			result.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			result.setException(exception);
			result.getException().seteCode(attributes.getValue(0));
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
			result.getMeta().getUserCode();
		} else if ("userPassword".equals(tagName)) {
			result.getMeta().setPassword(data);
		} else if ("serverTime".equals(tagName)) {
			result.getMeta().setServerTime(data);
		} else if ("result".equals(tagName)) {
			result.setResult(data);
		} else if ("exception".equals(tagName)) {
			result.getException().setErrMessage(data);
		}
		
	}

}
