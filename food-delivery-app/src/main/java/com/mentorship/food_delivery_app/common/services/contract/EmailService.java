package com.mentorship.food_delivery_app.common.services.contract;

public interface EmailService {
    void sendEmailAsync(String to, String subject, String body);
}
