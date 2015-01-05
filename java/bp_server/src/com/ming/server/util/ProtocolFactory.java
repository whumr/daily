package com.ming.server.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ming.server.common.SystemConfig;
import com.ming.server.common.domain.Constants;
import com.ming.server.common.domain.Constants.DEFAULT_VALUE;
import com.ming.server.common.domain.Protocol;

public class ProtocolFactory {
	
	private static Logger logger = Logger.getLogger(ProtocolFactory.class);
	
	public static Map<String, ProtocolUtil> protocolUtilMap = new HashMap<String, ProtocolUtil>();
	
	private static Properties config = new Properties();
	
	private static Properties protocolVersion = new Properties();
	
	public static String default_version;
	
	public static void initUtil(InputStream in, boolean xml) throws IOException, Exception {
		if(xml)
			config.loadFromXML(in);
		else
			config.load(in);
		if(config != null && !config.isEmpty()) {
			for(Iterator<Object> it = config.keySet().iterator(); it.hasNext();) {
				String version = (String)it.next();
				protocolUtilMap.put(version, (ProtocolUtil)Class.forName(config.getProperty(version)).newInstance());
			}
		}
	}

	public static void initProtocol(InputStream in, boolean xml) throws IOException, Exception {
		if(xml)
			protocolVersion.loadFromXML(in);
		else
			protocolVersion.load(in);
	}
	
	public static Protocol getProtocol(String version, byte type) throws Exception {
		Protocol protocol = (Protocol)Class.forName(protocolVersion
				.getProperty(version).split(Constants.DEFAULT_SPLITER)
				[type == Constants.REQUEST_CODE ? 0 : 1]).newInstance();
		protocol.setVersion(Integer.parseInt(version, 2));
		protocol.setType(type);
		return protocol;
	}
	
	public static ProtocolUtil getProtocolUtil() {
		if(default_version == null)
			default_version = SystemConfig.getStringParameter(DEFAULT_VALUE.DEFAULT_VERSION);
		return getProtocolUtil(default_version);
	}

	public static ProtocolUtil getProtocolUtil(DataInputStream in) throws IOException {
		return getProtocolUtil(Integer.toBinaryString(in.readInt()));
	}

	public static ProtocolUtil getProtocolUtil(String version) {
		ProtocolUtil protocolUtil = protocolUtilMap.get(version);
		if(protocolUtil == null)
			logger.error("no ProtocolUtil version " + version + " matched....");
		protocolUtil.setVersion(version);
		return protocolUtil;
	}
}