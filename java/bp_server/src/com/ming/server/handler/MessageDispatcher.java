package com.ming.server.handler;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;

import com.ming.server.common.domain.Protocol;
import com.ming.server.common.domain.Request;
import com.ming.server.listener.MessageQueue;

public class MessageDispatcher extends Thread {
	
	private ApplicationContext context;

	public MessageDispatcher() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MessageDispatcher(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void run() {
		while(true) {
			Protocol protocol = null;
			if((protocol = MessageQueue.getMessage()) != null) {
				new MessageRunner(protocol).start();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MessageRunner extends Thread {
		
		private Protocol protocol;
		
		public MessageRunner(Protocol protocol) {
			super();
			this.protocol = protocol;
		}
		
		public void run() {
			if(protocol instanceof Request) {
				Request request = (Request)protocol;
				HandlerMap handlerMap = HandlerMap.getHandlerMap(context);
				Object o = handlerMap.getHandlerObject(request.getOpCode(), context);
				Method m;
				try {
					m = handlerMap.getHandlerMethod(request.getOpCode(), context);
					m.invoke(o, request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				
			}
		}
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}
}
