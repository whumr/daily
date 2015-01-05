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
	 * ����presence
	 * 
	 * @param presence
	 */
	public void processPresence(Presence presence) {
		Element x = presence.getChildElement(TAG.X, NAMESPACE.MUC);
		//������ʷ��Ϣ
		if (x.element(TAG.HISTORY) != null)
			sendGroupHisMessage(presence);
	}
	
	/**
	 * ������ʷ��Ϣ
	 * 
	 * @param presence
	 */
	private void sendGroupHisMessage(Presence presence) {
		//��ǰʱ��
		long time = System.currentTimeMillis();
		String member_jid = presence.getFrom().toBareJID();
		JID group = presence.getTo();
		long group_id = -1;
		//ȡȺid
		try {
			group_id = Long.parseLong(group.getNode());
		} catch (Exception e) {
			return;
		}
		//ɾ��Ⱥ��Ա
		try {
			if (groupDbManager.isGroupMember(group_id, member_jid)) {
				//ȡ��������
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