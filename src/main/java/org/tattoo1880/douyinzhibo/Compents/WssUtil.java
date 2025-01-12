package org.tattoo1880.douyinzhibo.Compents;


import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.tattoo1880.douyinzhibo.Entities.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

@Component
@Slf4j
public class WssUtil {
	
	
	
	@Autowired
	R2dbcEntityTemplate r2dbcEntityTemplate;
	
	private final RabbitTemplate rabbitTemplate;
	
	
	public WssUtil(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	
	
	
	

	public void wssconnect(String url,String wssUrl) {
		
		Map<String, String> roomInfo = new WssUtil(rabbitTemplate).getRoomInfo(url);
		String ttwid = roomInfo.get("ttwid");
		String roomid = roomInfo.get("roomId");
		
		OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder()
//				.url("wss://webcast5-ws-web-lq.douyin.com/webcast/im/push/v2/?app_name=douyin_web&version_code=180800&webcast_sdk_version=1.0.14-beta.0&update_version_code=1.0.14-beta.0&compress=gzip&device_platform=web&cookie_enabled=true&screen_width=1440&screen_height=900&browser_language=zh-CN&browser_platform=MacIntel&browser_name=Mozilla&browser_version=5.0%20(Macintosh;%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/131.0.0.0%20Safari/537.36&browser_online=true&tz_name=Asia/Shanghai&cursor=t-1733294653776_r-1_d-1_u-1_h-7444443792379368475&internal_ext=internal_src:dim|wss_push_room_id:7444433701817142066|wss_push_did:7444075227670644265|first_req_ms:1733294653696|fetch_time:1733294653776|seq:1|wss_info:0-1733294653776-0-0|wrds_v:7444443848966670213&host=https://live.douyin.com&aid=6383&live_id=1&did_rule=3&endpoint=live_pc&support_wrds=1&user_unique_id=7444075227670644265&im_path=/webcast/im/fetch/&identity=audience&need_persist_msg_count=15&insert_task_id=&live_reason=&room_id=7444433701817142066&heartbeatDuration=0&signature=fZCpNb7oVQAVGwom")
				.url(wssUrl)
				.header("User-Agent","\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
				.addHeader("Connection", "Upgrade")
				.addHeader("Upgrade", "websocket")
				.addHeader("cookie","ttwid="+ttwid)
				.build();
		
		WebSocketListener listener = new WebSocketListener() {
			@Override
			public void onOpen(WebSocket webSocket, Response response) {
				System.out.println("Opened connection:" + response.message());
				
				//! todo 设置一个定时任务10分钟后自动关闭
				Executors.newSingleThreadScheduledExecutor().schedule(
						() -> {
							webSocket.close(1000,"close");
						},
						600,
						java.util.concurrent.TimeUnit.SECONDS
				);
			}
			
			@Override
			public void onMessage(WebSocket webSocket, String text) {
				System.out.println("Received text message: " + text);
				
			}
			
			@Override
			public void onMessage(WebSocket webSocket, okio.ByteString bytes) {
//				System.out.println("Received binary message: " + bytes);
				
				try {
					
					Douyin.PushFrame frame = Douyin.PushFrame.parseFrom(bytes.toByteArray());
//					System.out.println(frame);
					byte[] bytes1 = WssUtil.decompressGzip(frame.getPayload().toByteArray());
					Douyin.Response response = Douyin.Response.parseFrom(bytes1);
//					System.out.println(response);
					
					if (response.getNeedAck()){
//						System.out.println("======"  +  response.getNeedAck());
						
						Douyin.PushFrame.Builder builder = Douyin.PushFrame.newBuilder();
						builder.setPayloadType("ack")
								.setPayload(Douyin.Response.getDefaultInstance().getInternalExtBytes())
								.setLogID(frame.getLogID());
						
						webSocket.send(ByteString.of(builder.build().toByteArray()));
						
						
						
					}
					
					
					for (Douyin.Message item : response.getMessagesList()) {
						if (!item.getMethod().equals("WebcastChatMessage")) {
//							System.out.println("====");
//							System.out.println(item.getMethod());
//							System.out.println("====");

							if(item.getMethod().equals("WebcastGiftMessage")) {
								System.out.println("礼物消息");
								Douyin.GiftMessage giftMessage = Douyin.GiftMessage.parseFrom(item.getPayload());

								Douyin.User giftuser = giftMessage.getUser();
								String usernickname = giftuser.getNickName();
								long id = giftuser.getId();
								String iconuri = giftMessage.getGift().getImage().getUrlListList(0);
								long comboCount = giftMessage.getComboCount();

//								System.out.println("=====");
//								System.out.println(giftMessage.getGift().getImage().getUrlListList(0));
//								System.out.println("=====");

								System.out.println(usernickname + "(Id:" + id + ") 送出了 " + iconuri + "*" + comboCount);

								//todo
								Gift gift1 = new Gift("gift",usernickname,String.valueOf(id),iconuri,String.valueOf(comboCount));
								Gson gson = new Gson();
								String giftMessageJson = gson.toJson(gift1);
								log.info("用户{}送出了{}*{}",usernickname,iconuri,comboCount);
								rabbitTemplate.convertAndSend("wssend",giftMessageJson);




//								System.out.println(giftMessage);

							} else if (item.getMethod().equals("WebcastMemberMessage")) {
								Douyin.MemberMessage memberMessage = Douyin.MemberMessage.parseFrom(item.getPayload());

								String nickName = memberMessage.getUser()
								                               .getNickName();
								String id = String.valueOf(memberMessage.getUser()
								                                     .getId());
								EnterRoom enterRoom = new EnterRoom("enter",nickName,id);
								Gson gson = new Gson();
								String enterRoomJson = gson.toJson(enterRoom);
								log.info("用户{}进入房间",nickName);
								rabbitTemplate.convertAndSend("wssend",enterRoomJson);


							}
							continue;
						}



						
						Douyin.ChatMessage chatMessage = Douyin.ChatMessage.parseFrom(item.getPayload());
						String info = String.format("【%s -- %s -- %s】%s",
								chatMessage.getUser().getNickName(),
								chatMessage.getUser().getId(),
								chatMessage.getUser().getShotId(),
								chatMessage.getContent());
						log.info(info);
						
						String uid = String.valueOf(chatMessage.getUser().getId());
						String shortId = String.valueOf(chatMessage.getUser().getShotId());
						String nickname = chatMessage.getUser().getNickName();

						TContent tContent = new TContent();
						tContent.setUid(uid);
						tContent.setContent(chatMessage.getContent());
						tContent.setRoomId(roomid);
						tContent.setNickname(nickname);


						
						TUser user = new TUser();
						user.setUid(uid);
						user.setShortId(shortId);
						user.setNickname(nickname);
						
						//! 发送消息
//						rabbitTemplate.convertAndSend("wssend",info);
						ChatMessage chatMessage1 = new ChatMessage("chat",nickname,uid,chatMessage.getContent());
						Gson gson = new Gson();
						String chatMessageJson = gson.toJson(chatMessage1);
						rabbitTemplate.convertAndSend("wssend",chatMessageJson);

						//? 保存到数据库
						r2dbcEntityTemplate.insert(tContent)
								.doOnSuccess(
										(void1) -> {
											log.info("insert success");
										}
								)
								.doOnError(
										(throwable) -> {
											log.error("insert error",throwable);
										}
								)
								.subscribe();
						
						r2dbcEntityTemplate.select(TUser.class)
								.matching(Query.query(Criteria.where("uid").is(uid)))
								.first()
								.flatMap(
										(tUser) -> {
											log.debug("{}已经存在",uid);
											return Mono.just(tUser);
										}
								
								)
								.switchIfEmpty(
										r2dbcEntityTemplate.insert(user)
												.then(Mono.just(user))
								)
								
								
								
								.doOnSuccess(
										(void1) -> {
											log.info("insert success");
										}
								)
								.doOnError(
										(throwable) -> {
											log.error("insert error",throwable);
										}
								)
								.subscribe();
						
						
					}
					
					
					
				} catch (InvalidProtocolBufferException e) {
					System.out.println("InvalidProtocolBufferException");
				} catch (IOException e) {
					System.out.println("IOException");
				}
			}
			
			@Override
			public void onClosing(WebSocket webSocket, int code, String reason) {
				System.out.println("Closing: " + reason);
			}
			
			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				System.out.println("Closed: " + reason);
			}
			
			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				t.printStackTrace();
			}
		};
		
		WebSocket webSocket = client.newWebSocket(request, listener);
		
		
		
		
//		{
//			try {
//				Thread.sleep(1000000);
//			} catch (InterruptedException e) {
//				System.out.println("test end");
//				throw new RuntimeException(e);
//			}
//		}
		
	}
	
	
	public Map<String,String> getRoomInfo(String url) {
		OkHttpClient okHttpClient = new OkHttpClient();
		
		String webUrl = url;
		
		Headers headers = new Headers.Builder()
				.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
				.add("cookie","__ac_nonce=067237180003cef2c4c0")
				.build();
		
		
		Request request = new Request.Builder()
				.url(webUrl)
				.headers(headers)
				.build();
		
		try {
			Response response = okHttpClient.newCall(request).execute();
			
			//? 获取response.cookie
			
			List<String> cookies = response.headers("Set-Cookie");
			
			String ttwid = null;
			
			for (String cookie : cookies) {
				
				//? 获取cookie中的ttwid
				if (cookie.contains("ttwid")) {
					ttwid = cookie.split(";")[0].split("=")[1];
					
				}
			}
			
			
			
			String html = response.body().string();
			
			//**** 使用正则表达式提取数据
			
			/*****
			 *
			 *     match_list = re.findall(r'"roomId\\":\\"(\d+)\\",', res.text)
			 *     room_id = match_list[0]
			 *     print(room_id)
			 * */
			Pattern pattern = Pattern.compile("\"roomId\\\\\":\\\\\"(\\d+)\\\\\",");
			Matcher matcher = pattern.matcher(html);
			
			String roomId = null;
			
			if (matcher.find()) {
				roomId = matcher.group(1);
			}
			
			System.out.println(roomId);
			System.out.println(ttwid);
			
			if (roomId == null || ttwid == null) {
				throw new RuntimeException("获取roomid或者ttwid失败");
			}
			
			return Map.of("roomId",roomId,"ttwid",ttwid);
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
			
			
		}
		
		
	}
	
	
	// GZIP 解压
	public static byte[] decompressGzip(byte[] compressed) throws IOException {
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed);
		     GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
		     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gzipInputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			return byteArrayOutputStream.toByteArray();
		}
	}

}
