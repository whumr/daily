package com.ming.server.common.domain;

public class BaseException extends Exception {

	private static final long serialVersionUID = -1256605477461559518L;

	private int exceptionCode;

	public BaseException() {
		this(0);
	}
	
	public BaseException(int exceptionCode) {
		super();
		this.exceptionCode = exceptionCode;
	}
	
	public BaseException(String message) {
		this(message, 0);
	}
	
	public BaseException(String message, int exceptionCode) {
		super(message);
		this.exceptionCode = exceptionCode;
	}
	
	public BaseException(String message, Throwable cause) {
        this(message, cause, 0);
    }

	public BaseException(String message, Throwable cause, int exceptionCode) {
		super(message, cause);
		this.exceptionCode = 0;
	}
	
	public BaseException(Throwable cause) {
		this(cause, 0);
	}
	
	public BaseException(Throwable cause, int exceptionCode) {
		super(cause);
		this.exceptionCode = 0;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
