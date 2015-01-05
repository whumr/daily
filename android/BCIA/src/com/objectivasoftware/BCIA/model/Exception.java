package com.objectivasoftware.BCIA.model;

public class Exception {
	private String eCode;
	private String errMessage;
	/**
	 * @return the eCode
	 */
	public String geteCode() {
		return eCode;
	}
	/**
	 * @param eCode the eCode to set
	 */
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	/**
	 * @return the errMessage
	 */
	public String getErrMessage() {
		return errMessage;
	}
	/**
	 * @param errMessage the errMessage to set
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
}
