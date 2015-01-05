package com.ming.server.common.domain;

public class Constants {

	public static final String SYSTEM_CONFIG_FILE = "config\\sys-config.xml";

	public static final byte RESPONSE_CODE = -1;

	public static final byte REQUEST_CODE = 1;
	
	public static final String DEFAULT_SPLITER = ",";
	
	public static final String DEFAULT_RESPONSE = "操作成功";
	
	public static final class SPRING_CONSTANTS {
		public static final String HANDLER_MAP = "handlerMap";
		public static final String HANDLER_SPLIT = ",";
	}
	
	public static final class DEFAULT_VALUE {
		public static final String DEFAULT_CHARSET = "default_charset";
		public static final String DEFAULT_VERSION = "default_version";
		public static final String PROTOCOL_CONFIG_FILE = "protocol_config";
		public static final String PROTOCOL_UTIL_CONFIG = "protocol_util_config";
		public static final String LOG4J_CONFIG = "log4j_config";
	}
	
	public static final class NET {
		public static final int UDP_PORT = 8131;
		public static final int TCP_PORT = 8132;
		public static final int BUFFER_SIZE = 8192;
	}
	
	public static final class PROTOCOL_TYPE {
		public static final int UFF_STRING = 0;
		public static final int BYTE = 1;
		public static final int BOOLEAN = 2;
		public static final int CHAR = 3;
		public static final int SHORT = 4;
		public static final int INT = 5;
		public static final int LONG = 6;
		public static final int FLOAT = 7;
		public static final int DOUBLE = 8;
		public static final int BYTE_ARRAY = 9;
	}
	
	public static final class COMMAND {
		public static final String SEPCHAR = "#";
		public static final int REGIST = 0x1;
		public static final int LOGON = 0x2;
		public static final int LOGOUT = 0x3;
	}
}
