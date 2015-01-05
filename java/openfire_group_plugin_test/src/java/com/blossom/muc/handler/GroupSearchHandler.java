package com.blossom.muc.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.jivesoftware.openfire.muc.MUCRole.Role;
import org.jivesoftware.openfire.muc.MUCUser;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.blossom.muc.GroupServiceImpl;
import com.blossom.muc.LocalGroup;
import com.blossom.muc.LocalGroupRole;
import com.blossom.muc.LocalGroupUser;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.entity.Group;
import com.blossom.test.util.GroupError;

public class GroupSearchHandler extends BaseHandler {

	public GroupSearchHandler(GroupServiceImpl service) {
		super(service);
	}

	public IQ handleIQ(IQ iq) {
		JID from = iq.getFrom();
		Element condition = iq.getChildElement().element(TAG.CONDITION);
		List<Group> list = null;
		IQ reply = null;
		try {
			//有条件，查询
			if (condition != null) {
				String params = condition.getText();
				Map<String, String> map = parseParams(params);
				list = groupDbManager.searchGroups(map, sevice_id);
				reply = packetUtil.createGroupSearchResult(iq, list, true);
			//没条件，查询用户所在组
			} else {
				list = groupDbManager.getGroupsByUserName(from.toBareJID(), sevice_id);
				MUCUser user = service.getGroupUser(from);
				for (Group group : list) {
					LocalGroup localGroup = service.getGroups().get(group.getId());
					if (localGroup == null) {
						localGroup = new LocalGroup(service, group.getGroup_name(), XMPPServer.getInstance().getPacketRouter());
						localGroup.setGroup(group);
						localGroup.setOwner(group.getCreator());
						service.addGroup(localGroup);
					}
					if (user instanceof LocalGroupUser) {
						LocalGroupUser localGroupUser = (LocalGroupUser)user;
						boolean is_creator = group.getCreator().equals(user.getAddress().toBareJID());
						LocalGroupRole role = new LocalGroupRole(service, localGroup, localGroupUser, 
								is_creator ? Role.moderator : Role.participant, 
										is_creator ? Affiliation.owner : Affiliation.member);
						localGroupUser.addGroupRole(group.getId(), role);
						localGroup.addMember(role);
					}
				}
				reply = packetUtil.createGroupSearchResult(iq, list, false);
			}
		} catch (SQLException e) {
			return packetUtil.createErrorIq(iq, GroupError.Condition.server_error);
		}
        return reply;
	}
	
	/**
	 * 解析url格式的参数
	 * e.g: a=xx&b=aa
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, String> parseParams(String params) {
		Map<String, String> map = new HashMap<String, String>();
		if (params == null || "".equals(params.trim()))
			return map;
		String array[] = params.split("&");
		for (String s : array) {
			int index = s.indexOf('=');
			if (index > 0) {
				String key = s.substring(0, index).trim();
				String value = s.substring(index + 1).trim();
				if (!"".equals(key) && !"".equals(value))
					map.put(key, value);
			}
		}
		return map;
	}
}
