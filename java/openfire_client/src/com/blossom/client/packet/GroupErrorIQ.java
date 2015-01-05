package com.blossom.client.packet;

public class GroupErrorIQ extends GroupIQ {

	public static final String ELEMENTNAME = "errormessage";
	public static final String NAMESPACE = "jabber:error";
	
	private String code;
	private String errortype;
	private String msg;
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append(ELEMENTNAME).append("\"");
        if (code != null && code.length() > 0)
            buf.append(" code=\"").append(code).append("\"");
        if (errortype != null && errortype.length() > 0)
        	buf.append(" type=\"").append(errortype).append("\">");
        if (msg != null)
        	buf.append(msg);
        buf.append("</errormessage>");
        return buf.toString();
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrortype() {
		return errortype;
	}

	public void setErrortype(String errortype) {
		this.errortype = errortype;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
