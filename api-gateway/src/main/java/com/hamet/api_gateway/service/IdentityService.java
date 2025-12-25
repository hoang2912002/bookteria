package com.hamet.api_gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hamet.api_gateway.dto.request.IntrospectRequest;
import com.hamet.api_gateway.dto.response.ApiResponse;
import com.hamet.api_gateway.dto.response.IntrospectResponse;
import com.hamet.api_gateway.repository.httpClient.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequestMapping
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        try {
            return identityClient.introspect(IntrospectRequest.builder().token(token).build()); 
        } catch (Exception e) {
            throw e;
        }
    }
}
