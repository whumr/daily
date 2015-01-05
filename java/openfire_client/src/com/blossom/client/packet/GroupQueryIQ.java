package com.blossom.client.packet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.util.StringUtils;

public class GroupQueryIQ extends GroupIQ {
	
	public static final String ELEMENTNAME = "query";
	public static final String NAMESPACE = "http://jabber.org/protocol/disco#items";
	
	private final List<Item> items = new CopyOnWriteArrayList<Item>();
	private Set set;
	private Condition condition;
	
//	<iq from='user1@352.cn' to='group.352.cn' type='get'>
//	<query xmlns='http://jabber.org/protocol/disco#items'>
//	<condition><![CDATA[id=123&name=²âÊÔ]]></condition>
//	</query>
//	</iq>
//	
//	<iq from='group.352.cn' to='user1@352.cn' type='result'>
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

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"" + NAMESPACE + "\">");
        if (condition != null) {
        	buf.append(condition.toXML());
        } else {
        	synchronized (items) {
        		for (Item item : items) {
        			buf.append(item.toXML());
        		}
        	}
        	if (set != null)
        		buf.append(set.toXML());
        }
        buf.append("</query>");
        return buf.toString();
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void addItem(Item e) {
		items.add(e);
	}
	
	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public static class Item {
		private String jid;
		private String name;
		
		public Item(String jid, String name) {
			this.jid = jid;
			this.name = name;
		}
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
            buf.append("<item");
            if (jid != null) 
                buf.append(" jid=\"").append(StringUtils.escapeForXML(jid)).append("\"");
            if (name != null) 
                buf.append(" name=\"").append(StringUtils.escapeForXML(name)).append("\"");
            buf.append("/>");
            return buf.toString();
		}
		
		public String getJid() {
			return jid;
		}
		public void setJid(String jid) {
			this.jid = jid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public static class Set {
		public static final String NAMESPACE = "http://jabber.org/protocol/rsm";
		
		private String page;
		private String pageSize;
		private String count;
		
		public Set(String page, String pageSize, String count) {
			this.page = page;
			this.pageSize = pageSize;
			this.count = count;
		}
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
            buf.append("<set xmlns=\"").append(NAMESPACE).append("\">");
            if (page != null) 
                buf.append("<page>").append(page).append("</page>");
            if (pageSize != null) 
            	buf.append("<pageSize>").append(pageSize).append("</pageSize>");
            if (count != null) 
            	buf.append("<count>").append(count).append("</count>");
            buf.append("</set>");
            return buf.toString();
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public String getPageSize() {
			return pageSize;
		}

		public void setPageSize(String pageSize) {
			this.pageSize = pageSize;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}
	}
	
	public static class Condition {
		private String id;
		private String name;
		
		public Condition() {
		}
		
		public Condition(String id) {
			this.id = id;
		}
		
		public Condition(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String toXML() {
			StringBuilder buf = new StringBuilder();
            buf.append("<condition><![CDATA[");
            if (id != null && !"".equals(id.trim())) 
                buf.append("id=").append(id);
            else if (name != null && !"".equals(name.trim()))
            	buf.append("name=").append(name);
            buf.append("]]></condition>");
            return buf.toString();
		}
	}
}
