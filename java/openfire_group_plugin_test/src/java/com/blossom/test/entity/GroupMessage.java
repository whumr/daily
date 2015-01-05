package com.blossom.test.entity;

import java.util.Date;

public class GroupMessage {
//	tb_user_group_message | CREATE TABLE `tb_user_group_message` (
//			  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//			  `sender` varchar(50) NOT NULL,
//			  `group_id` int(10) unsigned NOT NULL,
//			  `content` text,
//			  `create_time` timestamp
	
	private long id;
	private long group_id;
	private String sender;
	private String content;
	private Date create_time;
	
	public GroupMessage(long group_id, String sender, String content) {
		super();
		this.group_id = group_id;
		this.sender = sender;
		this.content = content;
	}

	public GroupMessage(long id, long group_id, String sender, String content,
			Date create_time) {
		super();
		this.id = id;
		this.group_id = group_id;
		this.sender = sender;
		this.content = content;
		this.create_time = create_time;
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
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
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
}
