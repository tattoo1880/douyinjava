package org.tattoo1880.douyinzhibo.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tattoo1880.douyinzhibo.Entities.Room;
import org.tattoo1880.douyinzhibo.Service.RoomProducer;
import org.tattoo1880.douyinzhibo.Service.WsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api")
public class MyWssController {
	
	private final WsService wsService;
	private final RoomProducer roomProducer;
	
	public MyWssController(WsService wsService, RoomProducer roomProducer) {
		this.wsService = wsService;
		this.roomProducer = roomProducer;
	}
	
	
	@PostMapping("/start_wss")
	public Mono startMyWs(@RequestBody Map<String,String> map){
		String url = map.get("url");
//		return wsService.myWsService(url);
		
		//todo 先立即返回ok,再后台执行wsService.myWsService(url)
		
		
		return Mono.fromRunnable(
				() -> {
					wsService.myWsService(url);
				}
		).subscribeOn(Schedulers.boundedElastic())
				.then(Mono.just("ok"));
		
		
		
	}
	
	
	
	
	@PostMapping("/send")
	public Mono send(@RequestBody Room room){
		roomProducer.sendtoQueue(room);
		return Mono.just("ok");
	}
	
	@GetMapping("/users/findall")
	public Flux findAll(){
		return wsService.findAll();
	}
	
}
