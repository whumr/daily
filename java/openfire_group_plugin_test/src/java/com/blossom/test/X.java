package com.blossom.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.util.StringUtils;
import org.xmpp.packet.Presence;

import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.ATTRIBUTE_VALUE;
import com.blossom.test.component.Constants.NAMESPACE;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;

public class X {

	static String s = "<iq from='hag66@shakespeare.lit/pda' id='disco2' to='macbeth.shakespeare.lit' type='get'>" +
			"<query xmlns='http://jabber.org/protocol/disco#items'><reason><![CDATA[id=123&=²âÊÔ]]></reason></query></iq>";
	
	public static void main(String[] args) throws Exception {
//		String x = "Chat";
//		System.out.println(Type.valueOf(x));
		
		GroupApply apply = new GroupApply(111, "avvv@b.cn");
		
		Presence pre = new Presence();
		pre.setFrom(apply.getGroup_id() + "@" + "b.cn");
		Element x = pre.addChildElement(TAG.X, NAMESPACE.JABBER_REGISTER);
		Element item = x.addElement(TAG.ITEM);
		item.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.MEMBER);
		item.addAttribute(ATTRIBUTE_TAG.JID, apply.getApply_user());
		item.addAttribute(ATTRIBUTE_TAG.NICK, apply.getApply_user().split("@")[0]);
		item.addAttribute(ATTRIBUTE_TAG.ROLE, GroupMember.ROLE_DEFAULT);
//		System.out.println(pre.toXML());
//		System.out.println(pre.toString());
		
		Document doc = DocumentHelper.parseText(s);
		Element root = doc.getRootElement();
		System.out.println(root.element("query").elementText("x"));
		
//		System.out.println(((Element)(root.elements().get(0))).asXML());
//		Element child = root.element("query");
//		printElement(root);
//		printElement(child);
//		System.out.println(child.elementText("reason"));
//		System.out.println(root.asXML());
//		System.out.println(root.getName());
		
		System.out.println(StringUtils.encodeBase64("ssss"));
		System.out.println(new String(StringUtils.decodeBase64("c3Nzcw==")));
//		
//		System.out.println(root.element("query").elementText("reason") == null);
//		
//		String s = root.element("query").elementText("reason");
//		Map<String, String> m = parseParams(s);
//		for (String key : m.keySet()) {
//			System.out.println(key + " = " + m.get(key));
//		}
		
//		IQ iq = new IQ();
//		iq.setTo("admin@mr.cn");
//		Element child = iq.setChildElement(TAG.QUERY, NAMESPACE.GROUP_APPLY);
//		child.addAttribute(ATTRIBUTE_TAG.APPLIER, "xx@mr.cn/" + String.valueOf(1231231));
//		child.addCDATA("xxx");
//		Element x = child.addElement("xx", "sadasfs");
//		x.addText("ÈöµÄ");
//		
//		System.out.println(iq.toXML());
//		System.out.println("-----------");
//		
//		IQ response = IQ.createResultIQ(iq);
//		Element e = iq.getChildElement().createCopy();
//		e.clearContent();
//		response.setChildElement(e);
//		System.out.println(response.toXML());
		
//		IQ q = PacketUtil.getInstance().createErrorIq("", iq.getTo().toString(), Condition.alread_applied);
//		System.out.println(q.toXML());
//		
//		IQ[] iqs = new IQ[2];
//		iqs[1] = new IQ();
//		for (int i = 0; i < iqs.length; i++) {
//			if(iqs[i] != null)
//				System.out.println(iqs[i].toString());
//		}
	}
	
	private static Map<String, String> parseParams(String params) {
		if (params == null || "".equals(params.trim()))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		String array[] = params.split("&");
		for (String s : array) {
			int index = s.indexOf('=');
			if (index > 0)
				map.put(s.substring(0, index), s.substring(index + 1));
		}
		return map;
	}
	
	private static void printElement(Element e) {
		System.out.println(e.getName());
		System.out.println(e.getNamespacePrefix() + " === " + e.getNamespaceURI());
		List<Attribute> list = (List<Attribute>)e.attributes(); 
		for(Attribute a : list) {
			System.out.println(a.getName() + " = " + a.getValue());
		}
		System.out.println("--------------");
	}

}
