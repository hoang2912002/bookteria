package com.hamet.chat.service;

import org.springframework.stereotype.Service;

import com.hamet.chat.entity.WebSocketSession;
import com.hamet.chat.repository.WebSocketSessionRepository;
import com.hamet.chat.repository.httpClient.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    public WebSocketSession create(WebSocketSession webSocketSession ){
        return webSocketSessionRepository.save(webSocketSession);
    }

    public void deleteSession(String socketSessionId){
        webSocketSessionRepository.deleteBySocketSessionId(socketSessionId);
    }
}
