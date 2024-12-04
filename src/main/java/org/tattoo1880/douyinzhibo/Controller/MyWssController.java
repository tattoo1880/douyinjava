package org.tattoo1880.douyinzhibo.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.tattoo1880.douyinzhibo.Service.WsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
	
	public MyWssController(WsService wsService) {
		this.wsService = wsService;
	}
	
	
	@PostMapping("/start_wss")
	public Mono startMyWs(@RequestBody Map<String,String> map){
		String url = map.get("url");
		wsService.myWsService(url);
		
		//todo 先立即返回ok,再后台执行wsService.myWsService(url)
		
//


		// 立即返回响应
		return Mono.just("ok");
		
		
	}
	
	
}
