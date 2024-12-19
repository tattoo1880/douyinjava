package org.tattoo1880.douyinzhibo.Service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
		try {
			// 解析 room 字符串为 JsonElement
			JsonObject jsonObject = JsonParser.parseString(room).getAsJsonObject();

			// 检查 "roomurl" 是否存在并且不为 null
			JsonElement roomUrlElement = jsonObject.get("roomurl");
			if (roomUrlElement != null && !roomUrlElement.isJsonNull()) {
				String roomid = roomUrlElement.getAsString();
				log.info("Received roomid: " + roomid);

				// 调用 WebSocket 服务
				wsService.myWsService(roomid).subscribe();
			} else {
				log.warn("No valid 'roomurl' found in the message.");
			}
		} catch (JsonParseException e) {
			log.error("Invalid JSON format received: " + room, e);
		} catch (Exception e) {
			log.error("Error processing message: " + room, e);
		}
	}
}