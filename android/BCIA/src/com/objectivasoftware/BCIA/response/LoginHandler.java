package com.objectivasoftware.BCIA.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.objectivasoftware.BCIA.model.Acls;
import com.objectivasoftware.BCIA.model.Exception;
import com.objectivasoftware.BCIA.model.Meta;
import com.objectivasoftware.BCIA.model.UserInfo;

public class LoginHandler extends DefaultHandler {
	private UserInfo userInfo = null;
	private String tagName;
	private String tempString;
	private Acls acl = null;
	private Meta meta = null;
	private Exception exception = null;

	@Override
	public void startDocument() throws SAXException {
		userInfo = new UserInfo();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		
		if ("meta".equals(localName)) {
			meta = new Meta();
			userInfo.setMeta(meta);
			tempString = "meta";
		} else if ("user".equals(localName)) {
			tempString = "user";
		}
		
		if ("acl".equals(localName)) {
			acl = new Acls();
			acl.setrCode(attributes.getLocalName(0).toString());
		} else if (("exception").equals(localName)) {
			exception = new Exception();
			userInfo.setException(exception);
			userInfo.getException().seteCode(attributes.getValue(0));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String element = new String(ch, start, length);
		setElement(tagName, element);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if ("meta".equals(localName)) {
			tempString = null;
		} else if ("user".equals(localName)) {
			tempString = null;
		}
		if ("acl".equals(localName)) {
			userInfo.addAclsList(acl);
		}
		tagName = null;
	}


	public void setElement(String tagName, String element) {
		if (("userCode").equals(tagName)) {
			if ("meta".equals(tempString)) {
				userInfo.getMeta().setUserCode(element);
			} else if ("user".equals(tempString)) {
				userInfo.setUserCode(element);
			}
		} else if (("userPassword").equals(tagName)) {
			if ("meta".equals(tempString)) {
				userInfo.getMeta().setPassword(element);
			} else if ("user".equals(tempString)) {
				userInfo.setUserPassword(element);
			}
		} else if (("serverTime").equals(tagName)) {
			userInfo.getMeta().setServerTime(element);
		} else if (("userName").equals(tagName)) {
			userInfo.setUserName(element);
		} else if (("dgrp").equals(tagName)) {
			userInfo.setAirGroup(element);
		} else if (("exception").equals(tagName)){
			userInfo.getException().setErrMessage(element);
		}
	}

	/**
	 * @return the userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}
}
