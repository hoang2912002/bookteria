package com.hamet.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.hamet.chat.dto.request.ChatMessageRequest;
import com.hamet.chat.dto.response.ChatMessageResponse;
import com.hamet.chat.entity.ChatMessage;

@Mapper(
    componentModel = "spring"
)
public interface ChatMessageMapper {
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    ChatMessage toChatMessage(ChatMessageRequest request);

    List<ChatMessageResponse> toChatMessageResponses(List<ChatMessage> chatMessages);
}
