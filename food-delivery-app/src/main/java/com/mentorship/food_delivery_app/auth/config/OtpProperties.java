package com.mentorship.food_delivery_app.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.otp")
public record OtpProperties(int expirationMinutes) {

    public OtpProperties {
        if (expirationMinutes <= 0) {
            throw new IllegalArgumentException("OTP expiration-minutes must be positive");
        }
    }
}
