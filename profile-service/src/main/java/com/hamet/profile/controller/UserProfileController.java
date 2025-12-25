package com.hamet.profile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamet.profile.dto.request.ProfileCreationRequest;
import com.hamet.profile.dto.response.UserProfileResponse;
import com.hamet.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    
    @GetMapping("")
    List<UserProfileResponse> getAllProfilesResponse() {
        return userProfileService.getAllProfiles();
    }
    
}
