package org.tattoo1880.douyinzhibo.Config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	public static final String QUEUE_NAME = "zbroom";
	public static final String QUEUE_NAME2 = "wssend";
	
	
	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME, true);
	}
	
	@Bean
	public Queue queue2() {
		return new Queue(QUEUE_NAME2, true);
	}
}
