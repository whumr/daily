package com.blossom.client.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

import com.blossom.client.packet.GroupErrorIQ;

public class GroupErrorIQProvider extends GroupIQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GroupErrorIQ error = new GroupErrorIQ();
		String code = parser.getAttributeValue("", "code");
		String type = parser.getAttributeValue("", "type");
		String msg = parser.nextText();
		error.setCode(code);
		error.setErrortype(type);
		error.setMsg(msg);
		return error;
	}

}
