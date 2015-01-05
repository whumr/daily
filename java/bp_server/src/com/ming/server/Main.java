package com.ming.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ming.server.common.SystemInitializer;
import com.ming.server.handler.MessageDispatcher;
import com.ming.server.listener.imp.TcpListener;
import com.ming.server.listener.imp.UdpListener;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		SystemInitializer.init();
		ApplicationContext context = new FileSystemXmlApplicationContext(new String[]{
				"config\\application-business.xml",
				"config\\application-dao.xml",
				"config\\application-handler.xml"
		});
		MessageDispatcher messageDispatcher = (MessageDispatcher)context.getBean("messageDispatcher");
		messageDispatcher.setContext(context);
		messageDispatcher.start();
		new TcpListener().start();
		new UdpListener().start();
	}
}