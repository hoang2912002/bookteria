package com.hamet.chat.entity;

import java.time.Instant;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @MongoId
    String id;

    @Indexed
    String conversationId;

    String message;

    ParticipantInfo sender;

    @Indexed
    Instant createdDate;
}
