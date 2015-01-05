package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.DeicingStnd;
import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.FlightDeicingInputPitem;
import com.objectivasoftware.BCIA.model.Meta;

public class ParksPitemsHandler extends DefaultHandler {
	private DeicingStnd deicingStnd;
	private int index = 0;
	public DeicingStnd getDeicingStnd() {
		return deicingStnd;
	}

	private FlightDeicingInputPitem pitem;
	private Meta meta;
	private Exception exception;

	private String tagName;

	public void startDocument() throws SAXException {
		deicingStnd = new DeicingStnd();
		
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes attr) throws SAXException {
		tagName = localName;

		if("pitems".equals(localName)) {
			pitem = new FlightDeicingInputPitem();
			pitem.setPcode(attr.getValue(0));
		} else if ("meta".equals(localName)) {
			meta = new Meta();
			deicingStnd.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			deicingStnd.setException(exception);
			deicingStnd.getException().seteCode(attr.getValue(0));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		
		if("pitems".equals(localName)){
			deicingStnd.addFlightDeicingInputPitem(pitem);
			pitem = null;
		}
		index = index + 1;
		tagName = null;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String element = new String(ch, start, length);
		setElement(tagName, element);

	}

	private void setElement(String tagName, String element) {
		if ("userCode".equals(tagName)) {
			deicingStnd.getMeta().setUserCode(element);
		} else if ("userPassword".equals(tagName)) {
			deicingStnd.getMeta().setPassword(element);
		} else if ("serverTime".equals(tagName)) {
			deicingStnd.getMeta().setServerTime(element);
		} else if ("stnd".equals(tagName)) {
			pitem.addStndList(element);
		} else if ("exception".equals(tagName)) {
			deicingStnd.getException().setErrMessage(element);
		}

	}
}
