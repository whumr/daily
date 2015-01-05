package com.blossom.test.entity;

import java.util.Date;

public class GroupSysMessage {

	public static final String STATUS_NEW = "0";
	public static final String STATUS_DONE = "9";
	
	public static final long DEFAULT_GROUP_ID = 0;

	public static final String DEFAULT_CREATOR = "sys";
	
	private long id;
	private long group_id;
	private String creator;
	private String receiver;
	private String status;
	private String content;
	private Date create_date;
	private Date processe_date;
	
	public GroupSysMessage() {
	}

	public GroupSysMessage(String receiver, String content) {
		this(DEFAULT_GROUP_ID, DEFAULT_CREATOR, receiver, content);
	}

	public GroupSysMessage(String creator, String receiver, String content) {
		this(DEFAULT_GROUP_ID, creator, receiver, content);
	}

	public GroupSysMessage(long group_id, String creator, String receiver, String content) {
		this.group_id = group_id;
		this.creator = creator;
		this.receiver = receiver;
		this.content = content;
	}

	public GroupSysMessage(long id, long group_id, String receiver,
			String status, String content, Date create_date, Date processe_date) {
		this.id = id;
		this.group_id = group_id;
		this.receiver = receiver;
		this.status = status;
		this.content = content;
		this.create_date = create_date;
		this.processe_date = processe_date;
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
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getProcesse_date() {
		return processe_date;
	}
	public void setProcesse_date(Date processe_date) {
		this.processe_date = processe_date;
	}
}
