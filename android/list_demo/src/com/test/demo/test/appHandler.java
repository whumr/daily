package com.test.demo.test;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class appHandler extends DefaultHandler {
	private String tagName;
	private apps apps;
	private app app;
	

	public apps getApps() {
		return apps;
	}


	public void setApps(apps apps) {
		this.apps = apps;
	}


	public void startDocument() throws SAXException {
		apps = new apps();
	}


	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attr) throws SAXException {
		tagName = localName;
		if ("app".equals(localName)) {
			app = new app();
//			apps.addmApp(app);
		} 
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if ("app".equals(localName)) {
			apps.addmApp(app);
		} 
		tagName = null;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String element = new String(ch, start, length);
		setElement(tagName, element);

	}

	private void setElement(String tagName, String element) {
		if ("name".equals(tagName)) {
			app.setPicture(element);
			
		} else if ("score".equals(tagName)) {
			app.setScore(element);
		} else if ("name".equals(tagName)) {
			app.setName(element);
		} else if ("author".equals(tagName)) {
			app.setAuthor(element);
		} else if ("price".equals(tagName)) {
			app.setPrice(element);
		} else if ("type".equals(tagName)) {
			app.setType(element);
		} else if ("viewNum".equals(tagName)) {
			app.setViewNum(element);
		} else if ("description".equals(tagName)) {
			app.setDescription(element);
		} 
	}
}
