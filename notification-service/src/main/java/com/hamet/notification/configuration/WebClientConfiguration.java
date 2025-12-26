package com.hamet.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.hamet.notification.repository.httpClient.EmailClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient(){
        return WebClient.builder()
            .baseUrl("https://api.resend.com")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    @Bean
    EmailClient emailClient(WebClient webClient){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(
                WebClientAdapter.create(webClient)
            ).build();
        
        return httpServiceProxyFactory.createClient(EmailClient.class);
    }
}
