package com.hamet.profile.service;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.entity.UserProfile;
import com.hamet.profile.exception.AppException;
import com.hamet.profile.exception.ErrorCode;
import com.hamet.profile.mapper.UserProfileMapper;
import com.hamet.profile.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(
    level = lombok.AccessLevel.PRIVATE,
    makeFinal = true
)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        try {
            UserProfile userProfile = userProfileMapper.toUserProfile(request);
            return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
        } catch (Exception e) {
            log.error("Error creating user profile: {}", e.getMessage());
            throw e;
        }
    }

    public UserProfileResponse getProfileById(String id) {
        try {
            UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
            return userProfileMapper.toUserProfileResponse(userProfile);    
        } catch (Exception e) {
            log.error("Error fetching user profile: {}", e.getMessage());
            throw e;
        }
    }
    
    public UserProfileResponse getProfileByUserId(String id) {
        try {
            UserProfile userProfile = userProfileRepository.findByUserId(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
            return userProfileMapper.toUserProfileResponse(userProfile);    
        } catch (Exception e) {
            log.error("Error fetching user profile: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UserProfileResponse> getAllProfiles(){
        try {
            return userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileResponse).toList();
        } catch (Exception e) {
            log.error("Error fetching all user profiles: {}", e.getMessage());
            throw e;
        }
    }
}
