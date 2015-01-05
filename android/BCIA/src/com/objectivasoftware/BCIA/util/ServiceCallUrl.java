package com.objectivasoftware.BCIA.util;

public class ServiceCallUrl {

	private static String HOST_NAME = "210.75.250.248:9080/omc2";// 3G 网络访问地址

//	private static String HOST_NAME = "10.10.1.130:9080/omc2";  //WIFI 网络地址
//	private static String HOST_NAME = "10.80.2.39:8080/omc2"; //测试地址1
//	private static String HOST_NAME = "unionflightdev.gicp.net:9080/omc2/";//测试地址2
	private static String HOST_BASE = "http://";
	public static String LOGIN_URL = HOST_BASE + HOST_NAME + "/pad/login.action";
	public static String DEICING_INFO_URL = HOST_BASE + HOST_NAME + "/pad/getFlightDeicingList.action";
	public static String SEAECH_INFO_URL = HOST_BASE + HOST_NAME + "/pad/getFlightMonitorList.action";
	public static String DEICING_INPUT_URL = HOST_BASE + HOST_NAME + "/pad/getFlightDeicing.action";
	public static String UPDATE_INFO_URL = HOST_BASE + HOST_NAME + "/pad/udateFlightDeicing.action";
	public static String PARK_INFO_URL = HOST_BASE + HOST_NAME + "/pad/getParkConf.action";
	public static String GET_SERVER_TIME_URL = HOST_BASE + HOST_NAME + "/pad/getServerTime.action";
	public static String GET_PARK_STNDS_URL = HOST_BASE + HOST_NAME + "/pad/getParkStnds.action";
	
	
	public static String VERSON_NAME = "1.0";
}
