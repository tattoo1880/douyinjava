package org.tattoo1880.douyinzhibo.Compents;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EchoHandler implements WebSocketHandler {
	
	private static final Logger log = LoggerFactory.getLogger(EchoHandler.class);
	private final WebSocketSessionManager sessionManager;
	
	
	public EchoHandler(WebSocketSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		sessionManager.register(session); // 注册会话到管理器
		log.info("Handle new session: {}", session.getId());
		
		try {
			sessionManager.broadcast("欢迎加入直播间"); // 使用管理器广播欢迎消息
		} catch (Exception e) {
			log.error("发送消息失败: {}", e.getMessage());
		}
		
		return Mono.never(); // 保持连接活跃
		
	}
}