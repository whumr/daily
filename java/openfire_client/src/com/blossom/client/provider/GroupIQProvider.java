package com.blossom.client.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public abstract class GroupIQProvider implements IQProvider {

	@Override
	public abstract IQ parseIQ(XmlPullParser parser) throws Exception;

}
