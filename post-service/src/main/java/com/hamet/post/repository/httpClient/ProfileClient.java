package com.hamet.post.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hamet.post.dto.response.ApiResponse;
import com.hamet.post.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.service.profile-service}")
public interface ProfileClient {
    @GetMapping(value = "/internal/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getProfileByUserId(@PathVariable String userId);
}
