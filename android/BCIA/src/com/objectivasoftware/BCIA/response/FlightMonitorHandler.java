package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.FlightInfo;
import com.objectivasoftware.BCIA.model.FlightMonitor;
import com.objectivasoftware.BCIA.model.Meta;

public class FlightMonitorHandler extends DefaultHandler {

	private FlightInfo flightInfo = null;
	private FlightMonitor flightMonitor = null;
	public FlightMonitor getFlightMonitor() {
		return flightMonitor;
	}

	private Meta meta = null;
	private Exception exception;
	private String tagName;

	public void startDocument() throws SAXException {
		flightMonitor = new FlightMonitor();
	}

	public void endDocument() throws SAXException {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		tagName = localName;
		if ("flightInfo".equals(localName)) {
			flightInfo = new FlightInfo();
		} else if ("meta".equals(localName)) {
			meta = new Meta();
			flightMonitor.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			flightMonitor.setException(exception);
			flightMonitor.getException().seteCode(attributes.getValue(0));
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if ("flightInfo".equals(localName))
			flightMonitor.addFlightInfo(flightInfo);
		
		tagName = null;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		String element = new String(ch, start, length);
		setElement(tagName, element);

	}

	public void setElement(String tagName, String element) {
		
		if ("userCode".equals(tagName)) {
			flightMonitor.getMeta().setUserCode(element);
		} else if ("userPassword".equals(tagName)) {
			flightMonitor.getMeta().setPassword(element);
		} else if ("serverTime".equals(tagName)) {
			flightMonitor.getMeta().setServerTime(element);
		} else if ("seqNo".equals(tagName)) {
			flightInfo.setSeqNo(element);
		} else if ("flgtId".equals(tagName)) {
			flightInfo.setFlgId(element);
		} else if ("regCode".equals(tagName)) {
			flightInfo.setRegCode(element);
		} else if ("airlineCode".equals(tagName)) {
			flightInfo.setAirlineCode(element);
		} else if ("flgtNo".equals(tagName)) {
			flightInfo.setFlgNo(element);
		} else if ("actypeCode".equals(tagName)) {
			flightInfo.setActypeCode(element);
		} else if ("schDttm".equals(tagName)) {
			flightInfo.setSchDttm(element);
		} else if ("mntAgent".equals(tagName)) {
			flightInfo.setMntAgent(element);
		} else if ("deicingNum".equals(tagName)) {
			flightInfo.setDeicingNum(element);
		} else if ("vipRank".equals(tagName)) {
			flightInfo.setVipRank(element);
		} else if ("deicingLabel".equals(tagName)) {
			flightInfo.setDeicingLabel(element);
		} else if ("controlStart".equals(tagName)) {
			flightInfo.setControlStart(element);
		} else if ("flightStatus".equals(tagName)) {
			flightInfo.setFlightStatus(element);
		} else if ("illegalLabel".equals(tagName)) {
			flightInfo.setIllegalLabel(element);
		} else if ("ewCode".equals(tagName)) {
			flightInfo.setEwCode(element);
		} else if ("lastUpdate".equals(tagName)) {
			flightInfo.setLastUpdate(element);
		} else if ("exception".equals(tagName)) {
			flightMonitor.getException().setErrMessage(element);
		}
	}
}
