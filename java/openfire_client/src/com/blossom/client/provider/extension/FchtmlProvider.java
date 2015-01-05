package com.blossom.client.provider.extension;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.blossom.client.packet.extension.Fchtml;

public class FchtmlProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser)
			throws Exception {
		String msg = parser.nextText();
		return new Fchtml(msg);
	}

}
