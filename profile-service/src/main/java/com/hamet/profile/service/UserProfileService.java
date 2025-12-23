package com.hamet.profile.service;

import org.apache.catalina.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.entity.UserProfile;
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
            UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("User profile not found"));
            return userProfileMapper.toUserProfileResponse(userProfile);    
        } catch (Exception e) {
            log.error("Error fetching user profile: {}", e.getMessage());
            throw e;
        }
    }
}
