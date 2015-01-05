package com.ming.server.handler;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.ming.server.common.domain.Constants.SPRING_CONSTANTS;

public class HandlerMap {

	private static HandlerMap handlerMap;
	
	private Map<Integer, String> map;
	
	public static HandlerMap getHandlerMap(ApplicationContext context) {
		if(handlerMap == null)
			handlerMap = (HandlerMap)context.getBean(SPRING_CONSTANTS.HANDLER_MAP);
		return handlerMap;
	}

	public String[] getHandlerValue(int key) {
		if(map.get(key) == null)
			return null;
		return map.get(key).split(SPRING_CONSTANTS.HANDLER_SPLIT);
	}
	
	public Object getHandlerObject(int key, ApplicationContext context) {
		return context.getBean(map.get(key).split(SPRING_CONSTANTS.HANDLER_SPLIT)[0]);
	}

	public Method getHandlerMethod(int key, ApplicationContext context) throws SecurityException, NoSuchMethodException {
		String methodName = map.get(key).split(SPRING_CONSTANTS.HANDLER_SPLIT)[1];
		Method method = getHandlerObject(key, context).getClass().
			getDeclaredMethod(methodName, MessageHandler.ARGS);
		return method;
	}
	
	public Map<Integer, String> getMap() {
		return map;
	}

	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}
	
	
}
