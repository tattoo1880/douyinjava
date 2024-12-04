package org.tattoo1880.douyinzhibo.Service;


import org.tattoo1880.douyinzhibo.Compents.GetWssUrl;
import org.tattoo1880.douyinzhibo.Compents.WssUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WsService {
	
	private WssUtil wssUtil;
	private GetWssUrl getWssUrl;
	
	
	public WsService(WssUtil wssUtil, GetWssUrl getWssUrl) {
		this.wssUtil = wssUtil;
		this.getWssUrl = getWssUrl;
	}
	
	
	public Mono myWsService(String url){
		String realWssUrl = getWssUrl.getWssUrl(url);
		return Mono.create(sink -> {
			wssUtil.wssconnect(url,realWssUrl);
		});
		
	}
	
}
