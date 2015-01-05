package com.ming.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConfig {

	private static Properties config = new Properties();
	
	public static void init(InputStream in, boolean xml) throws IOException {
		if(xml)
			config.loadFromXML(in);
		else
			config.load(in);
	}
	
	public static int getIntParameter(String key) {
		String value = config.getProperty(key);
		if(value != null)
			return Integer.parseInt(value);
		return -1;
	}

	public static boolean getBooleanParameter(String key) {
		String value = config.getProperty(key);
		if(value != null)
			return Boolean.parseBoolean(value);
		return false;
	}

	public static String getStringParameter(String key) {
		return config.getProperty(key);
	}
}
