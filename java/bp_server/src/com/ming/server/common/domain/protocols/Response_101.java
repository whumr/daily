package com.ming.server.common.domain.protocols;

import com.ming.server.common.domain.Constants.PROTOCOL_TYPE;
import com.ming.server.common.domain.Response;

public class Response_101 extends Response {

	private static final long serialVersionUID = 2669568858358772900L;

//	no type, version
	private static String[][] FIELDS = {
		//read
		{"setOpCode", "setResponseCode", "setContentLength", "setContent"},
		//write
		{"getOpCode", "getResponseCode", "getContentLength", "getContent"}
	};

	private static int[] FIELDS_TYPE = {PROTOCOL_TYPE.INT, PROTOCOL_TYPE.INT, PROTOCOL_TYPE.INT, PROTOCOL_TYPE.UFF_STRING};
	
	public String[][] getFieldOrder() {
		if(fieldOrder == null)
			fieldOrder = FIELDS;
		return fieldOrder;
	}
	
	public int[] getFieldType() {
		if(fieldType == null)
			fieldType = FIELDS_TYPE;
		return fieldType;
	}
}