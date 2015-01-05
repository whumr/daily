package com.blossom.test.component.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;

import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.NAMESPACE;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.entity.GroupMessage;

public class GroupPresenceHandler extends GroupPacketHandler {
	
	/**
	 * 处理presence
	 * 
	 * @param presence
	 */
	public void processPresence(Presence presence) {
		Element x = presence.getChildElement(TAG.X, NAMESPACE.MUC);
		//请求历史消息
		if (x.element(TAG.HISTORY) != null)
			sendGroupHisMessage(presence);
	}
	
	/**
	 * 请求历史消息
	 * 
	 * @param presence
	 */
	private void sendGroupHisMessage(Presence presence) {
		//当前时间
		long time = System.currentTimeMillis();
		String member_jid = presence.getFrom().toBareJID();
		JID group = presence.getTo();
		long group_id = -1;
		//取群id
		try {
			group_id = Long.parseLong(group.getNode());
		} catch (Exception e) {
			return;
		}
		//删除群成员
		try {
			if (groupDbManager.isGroupMember(group_id, member_jid)) {
				//取请求条件
				Element history = presence.getChildElement(TAG.X, NAMESPACE.MUC).element(TAG.HISTORY);
				Map<String, String> params = new HashMap<String, String>();
				putMapNotEmpty(params, history, ATTRIBUTE_TAG.MAXSTANZAS);
				putMapNotEmpty(params, history, ATTRIBUTE_TAG.SECONDS);
				putMapNotEmpty(params, history, ATTRIBUTE_TAG.SINCE);
				putMapNotEmpty(params, history, ATTRIBUTE_TAG.MAXCHARS);
				List<GroupMessage> list = groupDbManager.listGroupHisMessage(group_id, params, time);
				if (list != null && !list.isEmpty()) {
					for (GroupMessage msg : list) 
						sendPacket(packetUtil.createHisMessage(msg, member_jid));
				}
			}
		} catch (SQLException e) {
		}
	}
	
	private void putMapNotEmpty(Map<String, String> map, Element element, String key) {
		String value = element.attributeValue(key);
		if (value != null && !"".equals(value.trim()))
			map.put(key, value);
	}
}