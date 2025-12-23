package com.hamet.profile.mapper;

import org.mapstruct.Mapper;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper{
    UserProfile toUserProfile(ProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
