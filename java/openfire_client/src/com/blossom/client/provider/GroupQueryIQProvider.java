package com.blossom.client.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

import com.blossom.client.packet.GroupQueryIQ;

public class GroupQueryIQProvider extends GroupIQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GroupQueryIQ groupQueryIQ = new GroupQueryIQ();
//		<iq from='group.352.cn' to='user1@352.cn' type='result'>
//	    <query xmlns='http://jabber.org/protocol/disco#items'>
//	         <item jid='13@group.352.cn' name='²âÊÔÈº'/>
//	         <item jid='15@group.352.cn' name='²âÊÔÈº1'/>
//	         <item jid='17@group.352.cn' name='²âÊÔÈº2'/>
//	         <set xmlns='http://jabber.org/protocol/rsm'>
//				<page>1</page>
//				<pagesize>10</pagesize>
//				<count>8</count>
//	         </set>
//	    </query>
//	</iq>
        boolean done = false;
        GroupQueryIQ.Item item;
        GroupQueryIQ.Set set;
        String jid = "";
        String name = "";
        String page = "";
        String pagesize = "";
        String count = "";
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
                jid = parser.getAttributeValue("", "jid");
                name = parser.getAttributeValue("", "name");
            } else if (eventType == XmlPullParser.END_TAG && "item".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
                item = new GroupQueryIQ.Item(jid, name);
                groupQueryIQ.addItem(item);
            } else if (eventType == XmlPullParser.START_TAG && "set".equals(parser.getName())) {
            	page = parser.getAttributeValue("", "page");
            	pagesize = parser.getAttributeValue("", "pagesize");
            	count = parser.getAttributeValue("", "count");
            } else if (eventType == XmlPullParser.END_TAG && "set".equals(parser.getName())) {
            	set = new GroupQueryIQ.Set(page, pagesize, count);
            	groupQueryIQ.setSet(set);
            } else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return groupQueryIQ;
	}

}
