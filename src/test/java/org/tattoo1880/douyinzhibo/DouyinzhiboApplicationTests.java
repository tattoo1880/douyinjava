package org.tattoo1880.douyinzhibo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class DouyinzhiboApplicationTests {
	
//	@Test
//	void testplaywright() throws InterruptedException {
//
//		Playwright playwright = Playwright.create();
//		//todo 使用本地mac 的 chrome
//		String chromePath = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
//
//		// 启动浏览器，使用本地 Chrome
//		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
//				.setChannel("chrome") // 必须明确使用 Chrome
//				.setHeadless(false)   // 设置为非无头模式
//				.setExecutablePath(Paths.get(chromePath)) // 指定本地 Chrome 路径
//		);
//		BrowserContext context = browser.newContext();
//		Page page = context.newPage();
//
//		//! 新建一个空列表
//
//		List<String> wsUrls = new ArrayList();
//
//
//		// 监听 WebSocket 请求，确保在页面加载前设置好监听
//
//		page.onWebSocket(ws -> {
//			// 当 WebSocket 请求发起时，将请求的 URL 添加到列表中
//			wsUrls.add(ws.url());
//		});
//
//
//		// 打开页面
//		page.navigate("https://live.douyin.com/123539899933",new Page.NavigateOptions().setTimeout(60000));
//
//
//
//
//
//
//
//
//		// 等待一段时间以确保页面加载并可能触发 WebSocket 请求
//		Thread.sleep(15000);
//
//		// 输出捕获到的所有 WebSocket 请求
//		for (String wsUrl : wsUrls) {
//			System.out.println("Captured WebSocket URL: " + wsUrl);
//		}
//
//
//		// Close the browser
//		browser.close();
//
//
//		String finalWsUrl = null;
//		if (wsUrls.size() > 0) {
//
//			finalWsUrl = wsUrls.get(0);
//
//		}
//
//		if (finalWsUrl == null) {
//			System.out.println("未找到ws地址");
//			return;
//		}
//
//
//
//	}
}
