package com.blossom.muc;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.MUCUser;
import org.jivesoftware.openfire.muc.cluster.RoomAvailableEvent;
import org.jivesoftware.openfire.muc.spi.MultiUserChatServiceImpl;
import org.jivesoftware.openfire.muc.spi.RemoteMUCUser;
import org.jivesoftware.openfire.user.PresenceEventDispatcher;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.cache.CacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.blossom.muc.handler.GroupIQOwnerHandler;
import com.blossom.muc.handler.GroupIQRegisterHandler;
import com.blossom.muc.handler.GroupSearchHandler;
import com.blossom.test.component.listener.GroupMemberListener;
import com.blossom.test.dao.GroupDbManager;
import com.blossom.test.entity.Group;

public class GroupServiceImpl extends MultiUserChatServiceImpl {

	private static final Logger Log = LoggerFactory.getLogger(GroupServiceImpl.class);
	
	protected Map<Long, LocalGroup> groups = new ConcurrentHashMap<Long, LocalGroup>();
	protected Map<String, LocalGroupUser> users = new ConcurrentHashMap<String, LocalGroupUser>();

	private GroupSearchHandler searchHandler;
	private GroupIQOwnerHandler iqOwnerHandler;
	private GroupIQRegisterHandler iqRegisterHandler;
	private GroupDbManager groupDbManager;

	public GroupServiceImpl(String subdomain, String description, Boolean isHidden) {
		super(subdomain, description, isHidden);
	}

	public void initialize(JID jid, ComponentManager componentManager) {
		// 去除一些不必要的初始化操作
		searchHandler = new GroupSearchHandler(this);
		iqOwnerHandler = new GroupIQOwnerHandler(this);
		iqRegisterHandler = new GroupIQRegisterHandler(this);
		groupDbManager = GroupDbManager.getInstance();
		
		routingTable = XMPPServer.getInstance().getRoutingTable();
        router = XMPPServer.getInstance().getPacketRouter();
        
        GroupMemberListener groupMemberListener = new GroupMemberListener();
		PresenceEventDispatcher.addListener(groupMemberListener);
	}

	public void start() {
		// 启动定时任务，清理不活跃的群
		Log.info("MUCServiceImpl starting...");
	}
	
	public void processPacket(Packet packet) {
        if (!isServiceEnabled()) {
            return;
        }
        System.out.println("group\t" + packet.toXML());
        // The MUC service will receive all the packets whose domain matches the domain of the MUC
        // service. This means that, for instance, a disco request should be responded by the
        // service itself instead of relying on the server to handle the request.
        try {
            // Check if the packet is a disco request or a packet with namespace iq:register
            if (packet instanceof IQ) {
                if (process((IQ)packet)) {
                    return;
                }
            }
            // The packet is a normal packet that should possibly be sent to the room
            JID receipient = packet.getTo();
            Long group_id = null;
            try {
            	group_id = Long.parseLong(receipient.getNode());
            } catch (Exception e) {
			}
            getGroupUser(packet.getFrom(), group_id).process(packet);
        }
        catch (Exception e) {
            Log.error(LocaleUtils.getLocalizedString("admin.error"), e);
        }
    }

	// service处理群查找，新建等不针对特定群的操作，其它操作交给localuser去处理
	protected boolean process(IQ iq) {
		Element childElement = iq.getChildElement();
		String namespace = null;
		// Ignore IQs of type ERROR
		if (IQ.Type.error == iq.getType()) {
			return false;
		}
		if (iq.getTo().getResource() != null) {
			// Ignore IQ packets sent to room occupants
			return false;
		}
		if (childElement != null) {
			namespace = childElement.getNamespaceURI();
		}
		IQ reply = null;
		if ("jabber:iq:register".equals(namespace)) {
			reply = iqRegisterHandler.handleIQ(iq);
		} else if ("jabber:iq:search".equals(namespace)) {
		} else if ("http://jabber.org/protocol/disco#info".equals(namespace)) {
			reply = XMPPServer.getInstance().getIQDiscoInfoHandler().handleIQ(iq);
		} else if ("http://jabber.org/protocol/muc#owner".equals(namespace)) {
			reply = iqOwnerHandler.handleIQ(iq);
		} else if ("http://jabber.org/protocol/disco#items".equals(namespace)) {
			reply = searchHandler.handleIQ(iq);
		} else {
			return false;
		}
		if (reply != null)
			router.route(reply);
		return true;
	}
	
	public MUCUser getGroupUser(JID userjid) {
		return  getGroupUser(userjid, null);
	}

	public MUCUser getGroupUser(JID userjid, Long group_id) {
		LocalGroupUser user;
		synchronized (userjid.toBareJID().intern()) {
			user = users.get(userjid.toBareJID());
			if (user == null) {
				if (group_id != null) {
					// Check if the JID belong to a user hosted in another
					// cluster node
					LocalGroup localGroup = groups.get(group_id);
					if (localGroup != null) {
						MUCRole occupant = localGroup.getOccupantByFullJID(userjid);
						if (occupant != null && !occupant.isLocal()) {
							return new RemoteMUCUser(userjid, localGroup);
						}
					}
				}
				user = new LocalGroupUser(this, router, userjid);
				users.put(userjid.toBareJID(), user);
			}
		}
		return user;
	}
	
	public LocalGroup getGroup(Presence packet) {
		LocalGroup localGroup = new LocalGroup(this, packet.getTo().getNode(), router);
		Group group = new Group(packet.getFrom().toBareJID(), packet.getFrom().getNode());
		try {
			localGroup.setGroup(group);
			localGroup.setOwner(group.getCreator());
			localGroup.setPersistent(true);
			long group_id = groupDbManager.insertGroup(localGroup);
			group.setId(group_id);
			groups.put(group_id, localGroup);
			CacheFactory.doClusterTask(new RoomAvailableEvent(localGroup));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return localGroup;
	}
	
	public LocalGroup getGroup(Long groupId) {
		boolean loaded = false;
		LocalGroup localGroup = groups.get(groupId);
        if (localGroup == null) {
            // If the room is persistent load the configuration values from the DB
            try {
                // Try to load the room's configuration from the database (if the room is
                // persistent but was added to the DB after the server was started up or the
                // room may be an old room that was not present in memory)
        		Group group = groupDbManager.getGroupById(groupId);
        		if (group != null) {
        			localGroup = new LocalGroup(this, group.getGroup_name(), router);
        			localGroup.setCanAnyoneDiscoverJID(true);
        			localGroup.setCreationDate(group.getCreate_date());
        			localGroup.setGroup(group);
        			localGroup.setOwner(group.getCreator());
        			loaded = true;
        			groups.put(groupId, localGroup);
        		} 
            }
            catch (Exception e) {
                // The room does not exist so do nothing
                localGroup = null;
            }
        }
        if (loaded) {
            // Notify other cluster nodes that a new room is available
            CacheFactory.doClusterTask(new RoomAvailableEvent(localGroup));
        }
        return localGroup;
	}
	
	public void addGroup(LocalGroup group) {
		groups.put(group.getGroup().getId(), group);
	}

	public Map<Long, LocalGroup> getGroups() {
		return groups;
	}
	
}