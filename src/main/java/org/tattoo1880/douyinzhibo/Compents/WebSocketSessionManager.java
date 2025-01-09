package org.tattoo1880.douyinzhibo.Compents;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	
	public void register(WebSocketSession session) {
		sessions.put(session.getId(), session);
		log.info("Session registered: {}", session.getId());
		session.receive().doFinally(signalType -> {
			sessions.remove(session.getId());
			log.info("Session closed: {}", session.getId());
		}).subscribe(); // 订阅接收，保持连接活跃，并在关闭时移除
	}
	
	public void broadcast(String message) {
		log.info("Broadcasting message: {}", message);
		sessions.values().forEach(session -> {
			if (session.isOpen()) { // 检查会话是否仍然打开
				session.send(Mono.just(session.textMessage(message)))
						.doOnError(e -> log.error("Error sending message to session {}: {}", session.getId(), e.getMessage()))
						.subscribe();
			} else {
				log.warn("Session {} is closed, skipping message.", session.getId());
				sessions.remove(session.getId());
			}
		});
	}
	
	public boolean hasSession(String sessionId) {
		return sessions.containsKey(sessionId);
	}
}