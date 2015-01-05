package com.blossom.test.entity;

import java.util.Date;

public class GroupMember {

	public static final String ROLE_DEFAULT = "0";
	public static final String ROLE_CREATOR = "9";
	public static final String ROLE_ADMIN = "8";
	 
	private long id;
	private long group_id;
	private String member_jid;
	private Date join_date;
	private String role;
	private String member_nick;
	
	private boolean online;
	
	public GroupMember(long group_id, String member_jid) {
		this.group_id = group_id;
		this.member_jid = member_jid;
	}

	public GroupMember(long group_id, String member_jid, String member_nick) {
		this.group_id = group_id;
		this.member_jid = member_jid;
		this.member_nick = member_nick;
	}
	
	public GroupMember(String member_jid, String member_nick, String role) {
		this.member_jid = member_jid;
		this.member_nick = member_nick;
		this.role = role;
	}
	
	public GroupMember(long group_id, String member_jid, String member_nick, String role) {
		this.group_id = group_id;
		this.member_jid = member_jid;
		this.member_nick = member_nick;
		this.role = role;
	}
	
	public GroupMember(long id, long group_id, String member_jid,
			Date join_date, String role, String member_nick) {
		super();
		this.id = id;
		this.group_id = group_id;
		this.member_jid = member_jid;
		this.join_date = join_date;
		this.role = role;
		this.member_nick = member_nick;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public String getMember_jid() {
		return member_jid;
	}
	public void setMember_jid(String member_jid) {
		this.member_jid = member_jid;
	}
	public Date getJoin_date() {
		return join_date;
	}
	public void setJoin_date(Date join_date) {
		this.join_date = join_date;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getMember_nick() {
		return member_nick;
	}
	public void setMember_nick(String member_nick) {
		this.member_nick = member_nick;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
}
