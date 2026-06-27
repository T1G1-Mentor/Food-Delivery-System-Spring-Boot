package com.mentorship.food_delivery_app.auth.service.contract;

import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.auth.dto.LoginDto;
import com.mentorship.food_delivery_app.auth.dto.OtpRequestDto;
import com.mentorship.food_delivery_app.auth.dto.OtpVerificationRequestDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(CustomerRegistrationDto customerRegistrationDto);

    void requestOtp(OtpRequestDto otpRequestDto);

    String verifyOtp(OtpVerificationRequestDto otpRequestDto);
}
