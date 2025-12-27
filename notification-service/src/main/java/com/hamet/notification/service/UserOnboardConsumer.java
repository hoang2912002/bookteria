package com.hamet.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserOnboardConsumer {
    @KafkaListener(topics = "onboard-successful")
    public void listen(String message){
        log.info("Kafka message received: {}", message);
    }
}
