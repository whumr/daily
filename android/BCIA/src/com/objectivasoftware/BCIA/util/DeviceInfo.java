package com.objectivasoftware.BCIA.util;

public class DeviceInfo {

	private static String deviceIP;
	private static String deviceMac;
	private static String deviceVersion;
	private static String deviceUdid;
	public static String getDeviceUdid() {
		return deviceUdid;
	}

	public static void setDeviceUdid(String deviceUdid) {
		DeviceInfo.deviceUdid = deviceUdid;
	}

	public static String getDeviceVersion() {
		return deviceVersion;
	}

	public static void setDeviceVersion(String deviceVersion) {
		DeviceInfo.deviceVersion = deviceVersion;
	}

	public static String getDeviceMac() {
		return deviceMac;
	}

	public static void setDeviceMac(String deviceMac) {
		DeviceInfo.deviceMac = deviceMac;
	}

	public static String getDeviceIP() {
		return deviceIP;
	}

	public static void setDeviceIP(String deviceIP) {
		DeviceInfo.deviceIP = deviceIP;
	}
	
}
