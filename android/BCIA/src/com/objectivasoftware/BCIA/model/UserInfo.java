package com.objectivasoftware.BCIA.model;

import java.util.ArrayList;
import java.util.List;

public class UserInfo extends Omc{
	private String userCode;
	private String userPassword;
	private String userName;
	private String airGroup;
	private List<Acls> aclsList = new ArrayList<Acls>();
	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the airGroup
	 */
	public String getAirGroup() {
		return airGroup;
	}
	/**
	 * @param airGroup the airGroup to set
	 */
	public void setAirGroup(String airGroup) {
		this.airGroup = airGroup;
	}
	/**
	 * @return the aclsList
	 */
	public List<Acls> getAclsList() {
		return aclsList;
	}
	/**
	 * @param aclsList the aclsList to set
	 */
	public void addAclsList(Acls acls) {
		this.aclsList.add(acls);
	}
}

