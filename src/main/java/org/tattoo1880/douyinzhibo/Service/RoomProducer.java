package org.tattoo1880.douyinzhibo.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.tattoo1880.douyinzhibo.Entities.Room;

@Service
@Slf4j
public class RoomProducer {
	
	
	
	private final RabbitTemplate rabbitTemplate;
	//! objectmapper
	private final ObjectMapper objectMapper;
	
	
	public RoomProducer(RabbitTemplate rabbitTemplate,ObjectMapper objectMapper) {
		this.rabbitTemplate = rabbitTemplate;
		this.objectMapper = objectMapper;
	}
	
	
	public void sendtoQueue(Room room) {
		
		String message = null;
		
		try {
			message = objectMapper.writeValueAsString(room);
			rabbitTemplate.convertAndSend("zbroom", message);
		} catch (Exception e) {
			
			log.error("error:{}",e);
		}
		
	}
	
}
