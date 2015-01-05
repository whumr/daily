package com.blossom.test.entity;

import java.util.Date;

public class GroupApply {
	
	public static final String STATUS_NEW = "0";
	public static final String STATUS_ACCEPT = "1";
	public static final String STATUS_DENIED = "2";
	
	private long id;
	private long group_id;
	private String apply_user;
	private Date apply_date;
	private Date processe_date;
	private String status;
	
	public GroupApply(long group_id, String apply_user) {
		this.group_id = group_id;
		this.apply_user = apply_user;
	}

	public GroupApply(long id, long group_id, String apply_user,
			Date apply_date, Date processe_date, String status) {
		this.id = id;
		this.group_id = group_id;
		this.apply_user = apply_user;
		this.apply_date = apply_date;
		this.processe_date = processe_date;
		this.status = status;
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
	public String getApply_user() {
		return apply_user;
	}
	public void setApply_user(String apply_user) {
		this.apply_user = apply_user;
	}
	public Date getApply_date() {
		return apply_date;
	}
	public void setApply_date(Date apply_date) {
		this.apply_date = apply_date;
	}
	public Date getProcesse_date() {
		return processe_date;
	}
	public void setProcesse_date(Date processe_date) {
		this.processe_date = processe_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
