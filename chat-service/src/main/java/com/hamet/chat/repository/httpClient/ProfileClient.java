package com.hamet.chat.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hamet.chat.dto.response.ApiResponse;
import com.hamet.chat.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.service.profile-service}")
public interface ProfileClient {
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
