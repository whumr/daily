package com.blossom.client.entity;

public class Member extends Node {

	private String group_id;
	
	public Member(String jid, String name, String group_id) {
		super(jid, name, TYPE_MEMBER);
		this.group_id = group_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
}