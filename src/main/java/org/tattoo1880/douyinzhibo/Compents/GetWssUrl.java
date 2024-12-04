package org.tattoo1880.douyinzhibo.Compents;


import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component

public class GetWssUrl {
	
	
	
	Logger log = LoggerFactory.getLogger(GetWssUrl.class);
	
	
	
	
	public String getWssUrl(String url) {
		String wssUrl = "";
		Playwright playwright = Playwright.create();
		//todo 使用本地mac 的 chrome
		String chromePath = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
		
		// 启动浏览器，使用本地 Chrome
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
				.setChannel("chrome") // 必须明确使用 Chrome
				.setHeadless(false)   // 设置为非无头模式
				.setExecutablePath(Paths.get(chromePath)) // 指定本地 Chrome 路径
		);
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		
		//! 新建一个空列表
		
		List<String> wsUrls = new ArrayList<>();
		
		
		// 监听 WebSocket 请求，确保在页面加载前设置好监听
		
		page.onWebSocket(ws -> {
			// 当 WebSocket 请求发起时，将请求的 URL 添加到列表中
			wsUrls.add(ws.url());
		});
		
		
		// 打开页面
		page.navigate(url,new Page.NavigateOptions().setTimeout(120000));
		
		
		
		
		
		
		
		
		// 等待一段时间以确保页面加载并可能触发 WebSocket 请求
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
			log.error("error",e);
		}
		
		// 输出捕获到的所有 WebSocket 请求
		for (String wsUrl : wsUrls) {
			System.out.println("Captured WebSocket URL: " + wsUrl);
		}
		
		
		// Close the browser
		browser.close();
		
		
		String finalWsUrl = null;
		if (wsUrls.size() > 0) {
			
			finalWsUrl = wsUrls.get(0);
			
		}
		
		if (finalWsUrl == null) {
			System.out.println("未找到ws地址");
			return null;
		}
		
		
		
		return finalWsUrl;
	}
	
	
	


}
