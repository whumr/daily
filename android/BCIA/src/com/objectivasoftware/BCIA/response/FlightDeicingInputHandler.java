package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.FlightDeicingInput;
import com.objectivasoftware.BCIA.model.FlightDeicingInputPitem;
import com.objectivasoftware.BCIA.model.Meta;

public class FlightDeicingInputHandler extends DefaultHandler {

	private FlightDeicingInput flightDeicingInput = null;
	public FlightDeicingInput getFlightDeicingInput() {
		return flightDeicingInput;
	}

	private FlightDeicingInputPitem pitem = null;
	private Meta meta = null;
	private Exception exception = null;

	private String tagName;

	public void startDocument() throws SAXException {
		flightDeicingInput = new FlightDeicingInput();
	}

	public void endDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes attr) throws SAXException {
		tagName = localName;

		if("pitems".equals(localName)) {
			pitem = new FlightDeicingInputPitem();
			pitem.setPcode(attr.toString());
		} else if ("meta".equals(localName)) {
			meta = new Meta();
			flightDeicingInput.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			flightDeicingInput.setException(exception);
			flightDeicingInput.getException().seteCode(attr.getValue(0));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		
		if("pitems".equals(localName)){
			flightDeicingInput.addPitemsList(pitem);
			pitem = null;
		}
		tagName = null;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String element = new String(ch, start, length);
		setElement(tagName, element);

	}

	private void setElement(String tagName, String element) {
		if ("userCode".equals(tagName)) {
			flightDeicingInput.getMeta().setUserCode(element);
		} else if ("userPassword".equals(tagName)) {
			flightDeicingInput.getMeta().setPassword(element);
		} else if ("serverTime".equals(tagName)) {
			flightDeicingInput.getMeta().setServerTime(element);
		} else if ("flgtId".equals(tagName)) {
			flightDeicingInput.setFlightId(element);
		} else if ("deicingSeq".equals(tagName)) {
			flightDeicingInput.setDeicingSeq(element);
		} else if ("deicStnd".equals(tagName)) {
			flightDeicingInput.setDeicStnd(element);
		} else if ("dihdDttm".equals(tagName)) {
			flightDeicingInput.setDihdRDttm(element);
		} else if ("dipbDttm".equals(tagName)) {
			flightDeicingInput.setDipbDttm(element);
		} else if ("diinDttm".equals(tagName)) {
			flightDeicingInput.setDiinDttm(element);
		} else if ("stdiDttm".equals(tagName)) {
			flightDeicingInput.setStdiDttm(element);
		} else if ("eddiDttm".equals(tagName)) {
			flightDeicingInput.setEddiDttm(element);
		} else if("diegDttm".equals(tagName)) {
			flightDeicingInput.setDiegDttm(element);
		} else if("ditoDttm".equals(tagName)) {
			flightDeicingInput.setDitoDttm(element);
		} else if("stnd".equals(tagName)) {
			pitem.addStndList(element);
		} else if ("lastUpdate".equals(tagName)){
			flightDeicingInput.setLastUpdate(element);
		} else if ("exception".equals(tagName)) {
			flightDeicingInput.getException().setErrMessage(element);
		}

	}
}
