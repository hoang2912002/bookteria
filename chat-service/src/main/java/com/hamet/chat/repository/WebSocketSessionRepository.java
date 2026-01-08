package com.hamet.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hamet.chat.entity.WebSocketSession;

public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
    void deleteBySocketSessionId(String socketSessionId);
    List<WebSocketSession> findAllByUserIdIn(List<String> userIds);
}
