package com.mentorship.food_delivery_app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean
    public ExecutorService emailExecutor(){
        return Executors.
                newThreadPerTaskExecutor(Thread.ofVirtual().name("E-Mail-", 1).factory());
    }
}
