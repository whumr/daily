package com.blossom.client.provider.extension;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.blossom.client.packet.extension.GroupInformation;

public class GroupInformationProvider implements PacketExtensionProvider {

	@Override
	public PacketExtension parseExtension(XmlPullParser parser)
			throws Exception {
		String id = parser.getAttributeValue("", "id");
		String to = parser.getAttributeValue("", "to");
		GroupInformation groupInformation = new GroupInformation();
		if (id != null && !"".equals(id.trim()))
			groupInformation.setId(id.trim());
		if (to != null && !"".equals(to.trim()))
			groupInformation.setTo(to.trim());
        return groupInformation;
	}
}