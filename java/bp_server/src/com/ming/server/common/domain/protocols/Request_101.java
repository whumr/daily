package com.ming.server.common.domain.protocols;

import com.ming.server.common.domain.Request;
import com.ming.server.common.domain.Constants.PROTOCOL_TYPE;

public class Request_101 extends Request {

	private static final long serialVersionUID = 2669568858358772900L;

//	no type, version
	private static String[][] FIELDS = {
		//read
		{"setOpCode", "setSubCode", "setFromUserId", "setToUserId", "setContentLength", "setContent"},
		//write
		{"getOpCode", "getSubCode", "getFromUserId", "getToUserId", "getContentLength", "getContent"}
	};

	private static int[] FIELDS_TYPE = {PROTOCOL_TYPE.INT, PROTOCOL_TYPE.INT, PROTOCOL_TYPE.LONG, 
		PROTOCOL_TYPE.LONG, PROTOCOL_TYPE.INT, PROTOCOL_TYPE.UFF_STRING};
	
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