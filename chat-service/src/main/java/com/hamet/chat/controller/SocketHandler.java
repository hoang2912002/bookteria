package com.hamet.chat.controller;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.hamet.chat.dto.request.IntrospectRequest;
import com.hamet.chat.dto.response.IntrospectResponse;
import com.hamet.chat.entity.WebSocketSession;
import com.hamet.chat.service.IdentityService;
import com.hamet.chat.service.WebSocketSessionService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SocketHandler {
    SocketIOServer server;
    IdentityService identityService;
    WebSocketSessionService webSocketSessionService;

    @OnConnect
    public void clientConnected(SocketIOClient client){
        // Get token from request param
        String token = client.getHandshakeData().getSingleUrlParam("token");
        // Verify token
        IntrospectResponse iResponse = identityService.introspect(IntrospectRequest.builder().token(token).build());
        // If Token is invalid disconnect
        if(iResponse.isValid()){
            log.info("Client connected: {}", client.getSessionId());
            // Persist webSocketSession
            WebSocketSession webSocketSession = WebSocketSession.builder()
                .socketSessionId(client.getSessionId().toString())
                .userId(iResponse.getUserId())
                .createdAt(Instant.now())
                .build();
            webSocketSession = webSocketSessionService.create(webSocketSession);

        }
        else{
            log.error("Authentication fail: {} from connect socket", client.getSessionId());
            client.disconnect();
        }
    }

    @OnDisconnect
    public void clientDisconnected(SocketIOClient client){
        log.info("Client disconnected: {}", client.getSessionId());
        webSocketSessionService.deleteSession(client.getSessionId().toString());
    }

    // Sẽ được chạy khi bean được init
    @PostConstruct
    public void startServer(){
        server.start();
        server.addListeners(this);
        log.info("Socket server started");
    }


    @PreDestroy
    public void stopServer(){
        server.stop();
        log.info("Socket server stop");
    }
}
