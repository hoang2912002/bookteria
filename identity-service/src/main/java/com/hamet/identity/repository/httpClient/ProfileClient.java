package com.hamet.identity.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hamet.identity.dto.request.ProfileCreateRequest;
import com.hamet.identity.dto.response.UserProfileResponse;
import com.hamet.identity.service.AuthenticationRequestInterceptor;

@FeignClient(name = "profile-service", url = "${app.service.profile-service}",
    configuration = {AuthenticationRequestInterceptor.class}
    // Thêm token vào header
)
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreateRequest createRequest);
}
