package com.blossom.client.entity;

public class Node {
	public static final int TYPE_FRIEND = 0, TYPE_GROUP = 1, 
			TYPE_FRIEND_GROUP = 2, TYPE_TITLE = 3, TYPE_MEMBER = 4, TYPE_MUC = 5;
	
	private String jid;
	private String name;
	private int type;

	public Node(String jid, String name, int type) {
		this.jid = jid;
		this.name = name;
		this.type = type;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String toString() {
		return this.name;
	}
}