package com.blossom.client.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

import com.blossom.client.packet.GroupMemberIQ;

public class GroupMemberIQProvider extends GroupIQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GroupMemberIQ groupMemberIQ = new GroupMemberIQ();
//		<iq from='13@group.352.cn' to='user1@352.cn' type='result'>
//		  <query xmlns='http://jabber.org/protocol/muc#admin'>
//		     <item affiliation='member' jid='user1@352.cn' nick='user1' role='owner' online='true'/>
//		     <item affiliation='member' jid='user2@352.cn' nick='user2' role='admin' online='false'/>
//		  </query>
//		</iq>
        boolean done = false;
        GroupMemberIQ.Item item;
        String affiliation = "";
        String jid = "";
        String nick = "";
        String role = "";
        String online = "";
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
            	affiliation = parser.getAttributeValue("", "affiliation");
                jid = parser.getAttributeValue("", "jid");
                nick = parser.getAttributeValue("", "nick");
                role = parser.getAttributeValue("", "role");
                online = parser.getAttributeValue("", "online");
            }
            else if (eventType == XmlPullParser.END_TAG && "item".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
                item = new GroupMemberIQ.Item(affiliation);
                item.setJid(jid);
                item.setNick(nick);
                item.setRole(role);
                item.setOnline(online);
                groupMemberIQ.addItem(item);
            }
            else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return groupMemberIQ;
	}

}
