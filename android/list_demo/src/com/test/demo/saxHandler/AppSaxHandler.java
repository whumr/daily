package com.test.demo.saxHandler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.test.demo.domain.App;

public class AppSaxHandler extends DefaultHandler {

	private Handler handler;
	private List<App> appList;
	private String tagName;
	private boolean tagEnd = false;
	private App app;

	public void startDocument() throws SAXException {
		appList = new ArrayList<App>();
	}

	public void endDocument() throws SAXException {
		Message msg = handler.obtainMessage();
		Log.d("sendMessage", "sendMessage");
		handler.sendMessage(msg);
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attr) throws SAXException {
		tagName = localName;
		tagEnd = false;
		if(tagName.equals("app"))
			app = new App();
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		tagName = localName;
		if(tagName.equals("app"))
			appList.add(app);
		tagEnd = true;
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(!tagEnd && !tagName.equals("app") && !tagName.equals("apps")) {
			String value = new String(ch, start, length);
			setElement(tagName, value);
		}
	}
	
	private void setElement(String tagName, String value) {
		if(tagName.equals("picture"))
			app.setPicture(value);
		else if(tagName.equals("score"))
			app.setScore(Integer.parseInt(value));
		else if(tagName.equals("name"))
			app.setName(value);
		else if(tagName.equals("author"))
			app.setAuthor(value);
		else if(tagName.equals("price"))
			app.setPrice(Double.parseDouble(value));
		else if(tagName.equals("type"))
			app.setType(value);
		else if(tagName.equals("viewNum"))
			app.setViewNum(Long.parseLong(value));
		else if(tagName.equals("description"))
			app.setDescription(value);
	}
	
	public AppSaxHandler(Handler handler) {
		super();
		this.handler = handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public List<App> getAppList() {
		return appList;
	}

}
