package org.tattoo1880.douyinzhibo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.tattoo1880.douyinzhibo.Compents.EchoHandler;
import org.tattoo1880.douyinzhibo.Compents.WebSocketSessionManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebFlux
public class WebSocketConfig {
	
	@Autowired
	WebSocketSessionManager sessionManager;
	@Bean
	public WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
	//todo 将/ws 路由到EchoHandler
	@Bean
	public HandlerMapping handlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/ws", new EchoHandler(sessionManager));
		int order = -1; // before annotated controllers
		
		return new SimpleUrlHandlerMapping(map, order);
	}
	
	
	
	
	
	
//	@Bean
//	public HandlerMapping handlerMapping() {
//
//		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
//		Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();
//		handlerMap.put("/ws", new EchoHandler());
//		simpleUrlHandlerMapping.setUrlMap(handlerMap);
//		simpleUrlHandlerMapping.setOrder(-1);
//		return simpleUrlHandlerMapping;
//	}

}