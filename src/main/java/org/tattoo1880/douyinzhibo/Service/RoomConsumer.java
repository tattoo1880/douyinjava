package org.tattoo1880.douyinzhibo.Service;


import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tattoo1880.douyinzhibo.Compents.WssUtil;


@Service
@Slf4j
public class RoomConsumer {
	
	@Autowired
	WsService wsService;


	@RabbitListener(queues = "zbroom")
	public void processMessage(String room) {
//		log.info("Received: " + room);
		
		String roomid = JsonParser.parseString(room).getAsJsonObject().get("roomurl").getAsString();
		log.info("Received: " + roomid);
		
		wsService.myWsService(roomid).subscribe();
		
		
		
		
	}
}
