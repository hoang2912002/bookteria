package com.hamet.notification.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hamet.eventDto.NotificationEvent;
import com.hamet.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent noti){
        log.info("Kafka message received: {}", noti);
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", noti.getRecipient());
        emailData.put("otp", "123456");
        emailData.put("link", "http://192.168.1.16:3000");
        emailService
            .sendVisualEmail(
                "hoang2912002@gmail.com",
                noti.getSubject(),
                emailData
            )
            .subscribe(
                null,
                ex -> log.error("Send email failed", ex)
            );
    }
}
