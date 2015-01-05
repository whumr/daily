package com.blossom.test.entity;

import java.util.Date;

public class GroupPriMessage {

	public static final String NEW = "0", SENT = "1";
	
	private long id;
	private String sender;
	private String receiver;
	private long group_id;
	private String content;
	private Date create_time;
	private String status;
	
	public GroupPriMessage(String sender, String receiver, long group_id, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.group_id = group_id;
		this.content = content;
	}
	
	public GroupPriMessage(long id, String sender, String receiver,
			long group_id, String content, Date create_time, String status) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.group_id = group_id;
		this.content = content;
		this.create_time = create_time;
		this.status = status;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
