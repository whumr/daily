package com.ming.server.common;

import java.io.FileInputStream;

import org.apache.log4j.xml.DOMConfigurator;

import com.ming.server.common.domain.Constants;
import com.ming.server.common.domain.Constants.DEFAULT_VALUE;
import com.ming.server.util.ProtocolFactory;

public class SystemInitializer {
	
	public static void init() throws Exception {
		//SystemConfig
		SystemConfig.init(new FileInputStream(Constants.SYSTEM_CONFIG_FILE), true);
		//ProtocolFactory
		ProtocolFactory.initProtocol(new FileInputStream(SystemConfig.getStringParameter(
				DEFAULT_VALUE.PROTOCOL_CONFIG_FILE)), true);
		ProtocolFactory.initUtil(new FileInputStream(SystemConfig.getStringParameter(
				DEFAULT_VALUE.PROTOCOL_UTIL_CONFIG)), true);
		//log4j
		DOMConfigurator.configure(SystemConfig.getStringParameter(DEFAULT_VALUE.LOG4J_CONFIG));
	}
}
