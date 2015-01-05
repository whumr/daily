package com.blossom.test.entity;

import java.util.Date;

public class Group {
	
	private long id;
	private String creator;
	private Date create_date;
	private String group_name;
	
	public Group() {
	}

	public Group(String creator, String group_name) {
		this.creator = creator;
		this.group_name = group_name;
	}

	public Group(long id, String creator, Date timestamp, String group_name) {
		this.id = id;
		this.creator = creator;
		this.create_date = timestamp;
		this.group_name = group_name;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date timestamp) {
		this.create_date = timestamp;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
