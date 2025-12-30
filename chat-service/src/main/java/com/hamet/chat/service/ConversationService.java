package com.hamet.chat.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hamet.chat.dto.request.ConversationRequest;
import com.hamet.chat.dto.response.ConversationResponse;
import com.hamet.chat.dto.response.UserProfileResponse;
import com.hamet.chat.entity.Conversation;
import com.hamet.chat.entity.ParticipantInfo;
import com.hamet.chat.exception.AppException;
import com.hamet.chat.exception.ErrorCode;
import com.hamet.chat.mapper.ConversationMapper;
import com.hamet.chat.repository.ConversationRepository;
import com.hamet.chat.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;

    ConversationMapper conversationMapper;

    public List<ConversationResponse> myConversations() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepository.findAllByParticipantIdsContains(userId);

        return conversations.stream().map(this::toConversationResponse).toList();
    }

    public ConversationResponse create(ConversationRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // Người gửi
        UserProfileResponse userInfo = profileClient.getProfile(userId).getResult();
        // Người nhận
        UserProfileResponse participantInfo = profileClient.getProfile(
            request.getParticipantIds().getFirst()
        ).getResult();

        if(userInfo == null || participantInfo == null){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        List<String> userIds = List.of(userId,participantInfo.getUserId());
        List<String> sorted = userIds.stream().sorted().toList();
        
        String userIdHash = generateParticipantHash(sorted);

        List<ParticipantInfo>participantInfos = List.of(
            ParticipantInfo.builder()
                .userId(userInfo.getUserId())
                .username(userInfo.getUsername())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .avatar(userInfo.getAvatar())
                .build(),
            ParticipantInfo.builder()
                .userId(participantInfo.getUserId())
                .username(participantInfo.getUsername())
                .firstName(participantInfo.getFirstName())
                .lastName(participantInfo.getLastName())
                .avatar(participantInfo.getAvatar())
                .build()
        );

        Conversation conversation = Conversation.builder()
            .type(request.getType())
            .participantsHash(userIdHash)
            .createdDate(Instant.now())
            .modifiedDate(Instant.now())
            .participants(participantInfos)
            .build();
        
        return toConversationResponse(conversationRepository.save(conversation));
    }

    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);

        conversation.getParticipants().stream()
                .filter(participantInfo -> !participantInfo.getUserId().equals(currentUserId))
                .findFirst().ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getUsername());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });

        return conversationResponse;
    }
}
