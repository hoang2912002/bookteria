package com.hamet.chat.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hamet.chat.dto.request.IntrospectRequest;
import com.hamet.chat.dto.response.ApiResponse;
import com.hamet.chat.dto.response.IntrospectResponse;
import com.hamet.chat.dto.response.UserProfileResponse;

@FeignClient(name = "identity-service", url = "${app.service.identity-service}")
public interface IdentityClient {
    @PostMapping("/auth/introspect")
    ApiResponse<IntrospectResponse> getProfile(@RequestBody IntrospectRequest token);
}
