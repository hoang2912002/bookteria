package com.hamet.notification.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.hamet.notification.dto.request.ResendRequest;
import com.hamet.notification.exception.AppException;
import com.hamet.notification.exception.ErrorCode;
import com.hamet.notification.repository.httpClient.EmailClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    TemplateEngine templateEngine;
    EmailClient emailClient;

    @NonFinal
    @Value("${resend.apiKey}")
    String apiKey;

    public Mono<Void> sendVisualEmail(String to, String subject, Map<String, Object> variables) {
        try {
            // 1. Render HTML ngay tại thread chính (để tránh lỗi ClassLoader như nãy)
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process("email-template", context);

            // 2. Tạo request gửi đến Resend
            ResendRequest request = ResendRequest.builder()
                .from("Acme <onboarding@resend.dev>") // Dùng domain mặc định của Resend để test
                .to(to)
                .subject(subject)
                .html(htmlContent)
                .build();

            // 3. Gọi API Resend (Mặc định là Non-blocking, không cần subscribeOn)
            return emailClient.sendEmail("Bearer " + apiKey, request)
                .doOnSuccess(res -> log.info("Email sent via Resend: {}", res))
                .doOnError(WebClientResponseException.class, e -> {
                        log.error("Status: {}", e.getStatusCode());
                        log.error("Body: {}", e.getResponseBodyAsString());
                    })
                .then();
        } catch (Exception e) {
            throw e;
        }
    }
}
