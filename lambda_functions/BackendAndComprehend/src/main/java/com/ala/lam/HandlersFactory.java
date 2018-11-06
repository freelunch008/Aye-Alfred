package com.ala.lam;

import java.util.HashMap;
import java.util.Map;

public class HandlersFactory {

	public static Map<String, IHandler> getHandlersMap() {
		return handlersMap;
	}

	public static void setHandlersMap(Map<String, IHandler> handlersMap) {
		HandlersFactory.handlersMap = handlersMap;
	}

	private static Map<String,IHandler> handlersMap;
	
	static
	{
		handlersMap= new HashMap<String, IHandler>();
		
		handlersMap.put("CREATE_HOST", new CreateHostHandler());
	}
	
}
