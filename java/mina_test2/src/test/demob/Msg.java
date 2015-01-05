package test.demob;

import java.io.Serializable;

public class Msg implements Serializable {

	private static final long serialVersionUID = 3352801416609666672L;
	
	private int code;
	private int fuserId;
	private int tuserId;
	private String msg;
	public String toString() {
		return new StringBuilder()
		.append("code:").append(code)
		.append("fuserId:").append(fuserId)
		.append("tuserId:").append(tuserId)
		.append("msg").append(msg)
		.toString();
	}
	public Msg() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Msg(int code, int fuserId, int tuserId, String msg) {
		super();
		this.code = code;
		this.fuserId = fuserId;
		this.tuserId = tuserId;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getFuserId() {
		return fuserId;
	}
	public void setFuserId(int fuserId) {
		this.fuserId = fuserId;
	}
	public int getTuserId() {
		return tuserId;
	}
	public void setTuserId(int tuserId) {
		this.tuserId = tuserId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
