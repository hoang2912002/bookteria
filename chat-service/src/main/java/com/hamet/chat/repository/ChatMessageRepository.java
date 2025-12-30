package com.hamet.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hamet.chat.entity.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>{
    List<ChatMessage> findAllByConversationIdOrderByCreatedDateDesc(String conversationId);

    @Aggregation(pipeline = {
        "{ $match: { conversationId: { $in: ?0 } } }",
        "{ $sort: { createdDate: -1 } }",
        "{ $group: { _id: \"$conversationId\", lastMessage: { $first: \"$$ROOT\" } } }",
        "{ $replaceRoot: { newRoot: \"$lastMessage\" } }"
    })
    List<ChatMessage> findLastMessageByConversationIds(List<String> conversationIds);
}
