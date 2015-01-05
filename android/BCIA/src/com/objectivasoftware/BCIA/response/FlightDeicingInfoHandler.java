package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.FlightDeicing;
import com.objectivasoftware.BCIA.model.FlightDeicingInfo;
import com.objectivasoftware.BCIA.model.Meta;

public class FlightDeicingInfoHandler extends DefaultHandler {

	private FlightDeicingInfo flightDeicingInfo = null;
	private FlightDeicing flightdeicing = null;
	public FlightDeicing getFlightdeicing() {
		return flightdeicing;
	}

	private Meta meta = null;
	private Exception exception = null;

	private String tagName;

	public void startDocument() throws SAXException {
		flightdeicing = new FlightDeicing();
	}


	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attr) throws SAXException {
		tagName = localName;
		if ("flightInfo".equals(tagName)) {
			flightDeicingInfo = new FlightDeicingInfo();
		} else if ("meta".equals(localName)) {
			meta = new Meta();
			flightdeicing.setMeta(meta);
		} else if ("exception".equals(localName)) {
			exception = new Exception();
			flightdeicing.setException(exception);
			flightdeicing.getException().seteCode(attr.getValue(0));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if ("flightInfo".equals(localName)) {
			flightdeicing.addFlightDeicingInfos(flightDeicingInfo);
		}
		tagName = null;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(!tagName.equals("apps") && !tagName.equals("app")) {
			String element = new String(ch, start, length);
			setElement(tagName, element);
		}
	}

	private void setElement(String tagName, String element) {
		if ("userCode".equals(tagName)) {
			flightdeicing.getMeta().setUserCode(element);
		} else if ("userPassword".equals(tagName)) {
			flightdeicing.getMeta().setPassword(element);
		} else if ("serverTime".equals(tagName)) {
			flightdeicing.getMeta().setServerTime(element);
		} else if ("flgtId".equals(tagName)) {
			flightDeicingInfo.setFlgtId(element);
		} else if ("regCode".equals(tagName)) {
			flightDeicingInfo.setRegCode(element);
		} else if ("airlineCode".equals(tagName)) {
			flightDeicingInfo.setAirlineCode(element);
		} else if ("flgtNo".equals(tagName)) {
			flightDeicingInfo.setFlgtNo(element);
		} else if ("actypeCode".equals(tagName)) {
			flightDeicingInfo.setActypeCode(element);
		} else if ("schDttm".equals(tagName)) {
			flightDeicingInfo.setSchDttm(element);
		} else if ("deicingNum".equals(tagName)) {
			flightDeicingInfo.setDeicingNum(element);
		} else if ("vipRank".equals(tagName)) {
			flightDeicingInfo.setVipRank(element);
		} else if ("deicingLabel".equals(tagName)) {
			flightDeicingInfo.setDeicingLabel(element);
		} else if ("controlStart".equals(tagName)) {
			flightDeicingInfo.setControlStart(element);
		} else if ("flightStatus".equals(tagName)) {
			flightDeicingInfo.setFlightStatus(element);
		} else if ("illegalLabel".equals(tagName)) {
			flightDeicingInfo.setIllegalLabel(element);
		} else if ("lastUpdate".equals(tagName)) {
			flightDeicingInfo.setLastUpdate(element);
		} else if ("seqNo".equals(tagName)) {
			flightDeicingInfo.setSeqNo(element);
		} else if ("mntAgent".equals(tagName)) {
			flightDeicingInfo.setMntAgent(element);
		} else if ("ewCode".equals(tagName)) {
			flightDeicingInfo.setEwCode(element);
		} else if ("completePct".equals(tagName)) {
			flightDeicingInfo.setCompletePct(element);
		} else if ("exception".equals(tagName)) {
			flightdeicing.getException().setErrMessage(element);
		}
	}

	
}
