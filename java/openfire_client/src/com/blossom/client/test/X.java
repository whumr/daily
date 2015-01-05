package com.blossom.client.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class X {

	public static final String s = "<iq type='get' from='13@group.352.cn' to='user2@352.cn'/>";
//	public static final String s = "<iq type='get' from='13@group.352.cn' to='user2@352.cn'>" +
//			"<query xmlns='jabber:iq:register' applier='user1@352.cn'>" +
//			"<applier>user1@352.cn</applier>" +
//			"<reason><![CDATA[user1申请加入群：测试群，备注：我是xx]]></reason>" +
//			"</query>" +
//			"</iq>";
	
	static String x = "<iq type='result' id='663-18' from='group.mr.cn' to='mr1@mr.cn/Smack'>" +
			"<errormessage xmlns='jabber:error' code='401' type='bad_request'>bad request.</errormessage>" +
			"</iq>";
	
	/**
	 * @param args
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws XmlPullParserException, IOException {
		XmlPullParser parser = new MXParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(new StringReader(x));

        int type = -1;
        while (type != XmlPullParser.END_DOCUMENT) {
        	type = parser.next();
        	System.out.println(type);
        	if (type == XmlPullParser.START_TAG && parser.getName().equals("errormessage"))
        		System.out.println(parser.nextText());
        }
		
//        int type = parser.getEventType();
//        while(type != XmlPullParser.START_TAG) {
//        	type = parser.next();
//        }
//        System.out.println(parser.getName());
//        System.out.println(parser.getAttributeValue("", "xxx"));
//        System.out.println(parser.getAttributeValue("", "from"));
//        System.out.println(parser.nextText());
        
//		Registration iq = parsePackets();
//		System.out.println(iq.toXML());
	}
	
	public static Registration parsePackets() throws XmlPullParserException {
		XmlPullParser parser = new MXParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(new StringReader(s));
		
        try {
            int eventType = parser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    return parseIQ(parser);
                }
                eventType = parser.next();
            } while (eventType != XmlPullParser.END_DOCUMENT);
        }
        catch (Exception e) {
        }
        return null;
    }
	
	public static Registration parseIQ(XmlPullParser parser) throws Exception {
		Registration iqPacket = null;

        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("query") && namespace.equals("jabber:iq:register")) {
                    iqPacket = parseRegistration(parser);
                }
            }else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("iq")) {
                    done = true;
                }
            }
        }
        System.out.println(iqPacket.toXML());
        return iqPacket;
     }

	private static Registration parseRegistration(XmlPullParser parser) throws Exception {
        Registration registration = new Registration();
        Map<String, String> fields = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                // Any element that's in the jabber:iq:register namespace,
                // attempt to parse it if it's in the form <name>value</name>.
                if (parser.getNamespace().equals("jabber:iq:register")) {
                    String name = parser.getName();
                    String value = "";
                    if (fields == null) {
                        fields = new HashMap<String, String>();
                    }

                    if (parser.next() == XmlPullParser.TEXT) {
                        value = parser.getText();
                    }
                    // Ignore instructions, but anything else should be added to the map.
                    if (!name.equals("instructions")) {
                        fields.put(name, value);
                    }
                    else {
                        registration.setInstructions(value);
                    }
                }
                // Otherwise, it must be a packet extension.
                else {
                    registration.addExtension(
                        PacketParserUtils.parsePacketExtension(
                            parser.getName(),
                            parser.getNamespace(),
                            parser));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        registration.setAttributes(fields);
        return registration;
    }
}
