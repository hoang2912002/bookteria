package com.hamet.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.ApiResponse;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("internal/users")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("")
    public ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest entity) {
        return ApiResponse.<UserProfileResponse>builder()
            .result(userProfileService.createProfile(entity))
            .build();
    }
    
    @GetMapping("/{id}")
    public ApiResponse<UserProfileResponse> getProfileByUserId(
        @PathVariable("id") String id
    ) {
        return ApiResponse.<UserProfileResponse>builder()
            .result(userProfileService.getProfileByUserId(id))
            .build();
    }

}
