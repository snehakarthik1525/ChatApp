package com.chat.chatapp;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import com.chat.chatapp.model.ChatMessage;
import com.chat.chatapp.model.ChatMessageGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChatApplicationTests {

	@Test
	public void contextLoads() {
	}

	    @Value("${local.server.port}")
	    private int port;
	    
	    private String URL;

	    private static final String SEND_MESSAGE_ENDPOINT = "/app/chat/";
	    private static final String SEND_MESSAGE_GROUP_ENDPOINT = "/app/chat/group/";
	    private static final String SEND_MESSAGE = "/app/topic/messages/";
	    private static final String SEND_MESSAGE_GROUP = "/app/topic/messages/group/";

	    private CompletableFuture<ChatMessage> completableFuture;
	    private CompletableFuture<ChatMessageGroup> completableFuture1;

	    @Before
	    public void setup() {
	        completableFuture = new CompletableFuture<>();
	        completableFuture1 = new CompletableFuture<>();
	        URL = "http://localhost:" + port+"/ws";
	    }


	    @Test
	    public void testSendMessageEndpoint() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {

	        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
	        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

	        @SuppressWarnings("deprecation")
			StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
	        }).get(1, SECONDS);

	        stompSession.subscribe(SEND_MESSAGE_ENDPOINT + 1, new SendMessageFrameHandler());
	        stompSession.send(SEND_MESSAGE + 1, "hello test");

	        //ChatMessage chatMsg = completableFuture.get(50, SECONDS);

	        assertNotNull(stompSession);
	    }

	    @Test
	    public void testSendMessageToGroupEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
	        String uuid = UUID.randomUUID().toString();

	        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
	        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

	        @SuppressWarnings("deprecation")
			StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
	        }).get(1, SECONDS);

	        stompSession.subscribe(SEND_MESSAGE_GROUP_ENDPOINT + uuid, new SendMessageToGroupFrameHandler());
	        stompSession.send(SEND_MESSAGE_GROUP + 1, null);
	        
	        //ChatMessageGroup chatMsgGrp = completableFuture1.get(50, SECONDS);
	        
	        assertNotNull(stompSession);
	    }

	    private List<Transport> createTransportClient() {
	        List<Transport> transports = new ArrayList<>(1);
	        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
	        return transports;
	    }

	    private class SendMessageFrameHandler implements StompFrameHandler {
	        @Override
	        public Type getPayloadType(StompHeaders stompHeaders) {
	            return ChatMessage.class;
	        }

	        @Override
	        public void handleFrame(StompHeaders stompHeaders, Object o) {
	            completableFuture.complete((ChatMessage) o);
	        }
	    }
	    
	    private class SendMessageToGroupFrameHandler implements StompFrameHandler {
	        @Override
	        public Type getPayloadType(StompHeaders stompHeaders) {
	            return ChatMessageGroup.class;
	        }

	        @Override
	        public void handleFrame(StompHeaders stompHeaders, Object o) {
	            completableFuture1.complete((ChatMessageGroup) o);
	        }
	    }
	    
}