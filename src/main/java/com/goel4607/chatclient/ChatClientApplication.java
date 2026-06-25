package com.goel4607.chatclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@EnableRetry
@SpringBootApplication
public class ChatClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatClientApplication.class, args);
    }

    @Bean
    RestClientCustomizer logbookCustomizer(LogbookClientHttpRequestInterceptor interceptor) {
        return restClient -> restClient.requestInterceptor(interceptor);
    }
}
