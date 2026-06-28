package com.mentorship.food_delivery_app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class AppConfig {
    @Bean
    public SecureRandom secureRandom()throws NoSuchAlgorithmException{
        try {
            return SecureRandom.getInstance("DRBG");
        }catch (NoSuchAlgorithmException e){
            return SecureRandom.getInstanceStrong();
        }
    }
}
