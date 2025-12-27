package com.hamet.notification.controller;

import org.springframework.web.bind.annotation.RestController;

import com.hamet.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("send-mail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;
    
    @PostMapping("")
    public Mono<Void> sendMail(@RequestBody String entity) {
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", "Công Hoàng");
        emailData.put("otp", "123456");
        emailData.put("link", "http://192.168.1.16:3000");
        return emailService.sendVisualEmail("hoang2912002@gmail.com","Test send mail",emailData);
    }
}
