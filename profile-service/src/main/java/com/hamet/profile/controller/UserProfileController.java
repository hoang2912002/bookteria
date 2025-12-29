package com.hamet.profile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.request.UpdateProfileRequest;
import com.hamet.profile.dto.response.ApiResponse;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("users")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    
    UserProfileService userProfileService;
    
    @GetMapping("/{id}")
    UserProfileResponse getProfile(@PathVariable("id") String id) {
        return userProfileService.getProfileById(id);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    List<UserProfileResponse> getAllProfilesResponse() {
        return userProfileService.getAllProfiles();
    }
    
    @PutMapping("/my-profile")
    ApiResponse<UserProfileResponse> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateMyProfile(request))
                .build();
    }
    
    @PutMapping("/avatar")
    ApiResponse<UserProfileResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateAvatar(file))
                .build();
    }
}
