package com.hamet.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/user-profile")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("")
    UserProfileResponse createProfile(@RequestBody ProfileCreationRequest entity) {
        
        return userProfileService.createProfile(entity);
    }
    

}
