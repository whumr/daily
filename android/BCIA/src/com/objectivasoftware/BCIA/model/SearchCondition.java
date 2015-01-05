package com.objectivasoftware.BCIA.model;

import java.io.Serializable;

public class SearchCondition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String data;
	private String fightNum;
	private String plane;
	private String schNum;
	private String company;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getFightNum() {
		return fightNum;
	}
	public void setFightNum(String fightNum) {
		this.fightNum = fightNum;
	}
	public String getPlane() {
		return plane;
	}
	public void setPlane(String plane) {
		this.plane = plane;
	}
	public String getSchNum() {
		return schNum;
	}
	public void setSchNum(String schNum) {
		this.schNum = schNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
}
