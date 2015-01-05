package com.objectivasoftware.BCIA.model;

abstract class Omc {
	private Meta meta;
	private Exception exception;
	/**
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}
	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
}