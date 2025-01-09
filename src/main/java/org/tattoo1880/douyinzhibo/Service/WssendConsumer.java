package org.tattoo1880.douyinzhibo.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tattoo1880.douyinzhibo.Compents.EchoHandler;
import org.tattoo1880.douyinzhibo.Compents.WebSocketSessionManager;

@Service
@Slf4j
public class WssendConsumer {
	
	private final WebSocketSessionManager sessionManager;
	
	public WssendConsumer(WebSocketSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public void sendMessageToAll(String message) {
		sessionManager.broadcast(message);
	}
	
	@RabbitListener(queues = "wssend")
	public void processMessage(String message) {
		log.info("Received message: " + message);
		try {
			sendMessageToAll(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
