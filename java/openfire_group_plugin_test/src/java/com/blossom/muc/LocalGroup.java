package com.blossom.muc;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.muc.ConflictException;
import org.jivesoftware.openfire.muc.ForbiddenException;
import org.jivesoftware.openfire.muc.HistoryRequest;
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.NotAcceptableException;
import org.jivesoftware.openfire.muc.RegistrationRequiredException;
import org.jivesoftware.openfire.muc.RoomLockedException;
import org.jivesoftware.openfire.muc.ServiceUnavailableException;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.jivesoftware.openfire.muc.spi.LocalMUCUser;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;

import com.blossom.muc.handler.GroupIQAdminHandler;
import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupMember;

public class LocalGroup extends LocalMUCRoom {

	private GroupIQAdminHandler iqAdminHandler;
	private GroupDbManager dbManager;
	private GroupServiceImpl service;
	private Map<String, LocalGroupRole> groupMembers = new ConcurrentHashMap<String, LocalGroupRole>();
	private String owner;
	private Group group;
	private long lastPacketTime;
	private boolean member_loaded = false;
	
	public LocalGroup() {
	}
	
	public LocalGroup(GroupServiceImpl service, String name, PacketRouter packetRouter) {
		this.service = service;
		this.name = name;
		this.router = packetRouter;
		this.iqAdminHandler = new GroupIQAdminHandler(service);
		this.dbManager = GroupDbManager.getInstance();
	}

	@Override
	public MUCRole joinRoom(String nickname, String password,
			HistoryRequest historyRequest, LocalMUCUser user, Presence presence)
			throws UnauthorizedException, UserAlreadyExistsException,
			RoomLockedException, ForbiddenException,
			RegistrationRequiredException, ConflictException,
			ServiceUnavailableException, NotAcceptableException {
		// FIXME Auto-generated method stub
		return super.joinRoom(nickname, password, historyRequest, user, presence);
	}
	
	public GroupIQAdminHandler getIqAdminHandler() {
		return iqAdminHandler;
	}

	public Map<String, LocalGroupRole> getGroupMembers() {
		if (!member_loaded) {
			try {
				List<GroupMember> members = dbManager.listGroupMembers(group.getId());
				for (GroupMember groupMember : members) {
					String jid = groupMember.getMember_jid();
					if (!groupMembers.containsKey(jid)) {
						LocalGroupUser user = new LocalGroupUser(service, router, new JID(jid));
						String role = groupMember.getRole();
						Affiliation affiliation = Affiliation.member;
						if (GroupMember.ROLE_CREATOR.equals(role)) 
							affiliation = Affiliation.owner;
						else if (GroupMember.ROLE_ADMIN.equals(role))
							affiliation = Affiliation.admin;
						addMember(new LocalGroupRole(user, affiliation, groupMember.getMember_nick()));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return groupMembers;
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void addMember(LocalGroupRole role) {
		groupMembers.put(role.getUserAddress().toBareJID(), role);
		if (role.getAffiliation() == Affiliation.owner)
			owner = role.getUserAddress().toBareJID();
	}
	
	public void removeMember(String jid) {
		groupMembers.remove(jid);
	}
	
	public boolean isMember(String jid) {
		return groupMembers.containsKey(jid);
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isOwner(String jid) {
		return owner.equals(jid);
	}

	public long getLastPacketTime() {
		return lastPacketTime;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
//		ExternalizableUtil.getInstance().writeBoolean(out, value)
		
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// FIXME Auto-generated method stub
		super.readExternal(in);
	}
	
	public MultiUserChatService getMUCService() {
        return service;
    }
}
