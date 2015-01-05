package com.blossom.client.util;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;

public class Session {
	private XMPPConnection connection;

    private String serverAddress;
    private String username;
    private String password;

    private String JID;
    
    private Map<String, IQ> iqMap = new HashMap<String, IQ>();
    
    private Map<String, Presence> preMap = new HashMap<String, Presence>();
   
    private static Session session;
    
    private Session() {
    }
    
    public static Session getSession() {
    	if (session == null)
    		session = new Session();
    	return session;
    }
    
    public void init(XMPPConnection connection, String username, String password) {
    	this.connection = connection;
    	this.serverAddress = connection.getServiceName();
    	this.username = username;
    	this.password = password;
    	this.JID = username + "@" + serverAddress;
    }

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
	
	public void addIq(IQ iq) {
		iqMap.put(iq.getPacketID(), iq);
	}
	
	public boolean containsIq(String packetId) {
		return iqMap.containsKey(packetId);
	}
	
	public IQ getIq(String packetId) {
		return iqMap.get(packetId);
	}
	
	public void removeIq(IQ iq) {
		iqMap.remove(iq.getPacketID());
	}

	public void addPre(Presence pre) {
		preMap.put(pre.getPacketID(), pre);
	}
	
	public Presence getPre(String packetId) {
		return preMap.get(packetId);
	}
	
	public void removePre(Presence pre) {
		preMap.remove(pre.getPacketID());
	}
	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJID() {
		return JID;
	}

	public void setJID(String jID) {
		JID = jID;
	}
}
