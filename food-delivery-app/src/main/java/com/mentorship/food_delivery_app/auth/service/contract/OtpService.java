package com.mentorship.food_delivery_app.auth.service.contract;

public interface OtpService {
    void sendOtp(String email);
    void verifyOtp(String email, String code);
}
