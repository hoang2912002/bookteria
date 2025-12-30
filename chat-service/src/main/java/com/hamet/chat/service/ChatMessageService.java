package com.hamet.chat.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOServer;
import com.hamet.chat.dto.request.ChatMessageRequest;
import com.hamet.chat.dto.response.ChatMessageResponse;
import com.hamet.chat.dto.response.UserProfileResponse;
import com.hamet.chat.entity.ChatMessage;
import com.hamet.chat.entity.Conversation;
import com.hamet.chat.entity.ParticipantInfo;
import com.hamet.chat.exception.AppException;
import com.hamet.chat.exception.ErrorCode;
import com.hamet.chat.mapper.ChatMessageMapper;
import com.hamet.chat.repository.ChatMessageRepository;
import com.hamet.chat.repository.ConversationRepository;
import com.hamet.chat.repository.httpClient.ProfileClient;

import ch.qos.logback.core.joran.action.AppenderAction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    SocketIOServer socketIOServer;

    ChatMessageMapper chatMessageMapper;
    public List<ChatMessageResponse> getMessages(String conversationId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
            () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)
        );
        conversation.getParticipants().stream().filter(p -> userId.equals(p.getUserId())).findAny().orElseThrow(
            () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)
        );

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByConversationIdOrderByCreatedDateDesc(conversationId);

        return chatMessages.stream().map(this::toChatMessageResponse).toList();
    }

    public ChatMessageResponse create(ChatMessageRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Conversation conversation = conversationRepository.findById(request.getConversationId()).orElseThrow(
            () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)
        );

        conversation.getParticipants().stream().filter(p -> userId.equals(p.getUserId())).findAny().orElseThrow(
            () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)
        );

        UserProfileResponse userInfo = profileClient.getProfile(
            userId
        ).getResult();

        if(userInfo == null){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(ParticipantInfo.builder()
            .userId(userInfo.getUserId())
            .username(userInfo.getUsername())
            .firstName(userInfo.getFirstName())
            .lastName(userInfo.getLastName())
            .avatar(userInfo.getAvatar())
            .build()
        );
        chatMessage.setCreatedDate(Instant.now());

        String message = chatMessage.getMessage();

        socketIOServer.getAllClients().forEach(c -> {
            c.sendEvent("message", message);
        });

        return toChatMessageResponse(chatMessageRepository.save(chatMessage));
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatMessageResponse chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);

        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getUserId()));
        return chatMessageResponse;
    }
}
