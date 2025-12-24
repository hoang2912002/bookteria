package com.hamet.identity.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hamet.identity.dto.request.ProfileCreateRequest;
import com.hamet.identity.dto.response.UserProfileResponse;

@FeignClient(name = "profile-service", url = "${app.service.profile-service}")
public interface ProfileClient {
    @PostMapping(value = "/internal/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreateRequest createRequest);
}
