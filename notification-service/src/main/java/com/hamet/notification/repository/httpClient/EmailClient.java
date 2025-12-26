package com.hamet.notification.repository.httpClient;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

import com.hamet.notification.dto.request.ResendRequest;

import reactor.core.publisher.Mono;

public interface EmailClient {
    @PostExchange("/emails")
    Mono<Map<String, Object>> sendEmail(@RequestHeader("Authorization") String token,@RequestBody ResendRequest request);
}
