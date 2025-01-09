package org.tattoo1880.douyinzhibo.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class TestProduct {
	
	private final RabbitTemplate rabbitTemplate;
	
	public TestProduct(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	//! 每隔5秒发送一次消息
//	@Scheduled(fixedRate = 5000)
	public void sendtoQueue() {
		rabbitTemplate.convertAndSend("wssend", "hello");
	}
	
}
