package com.mentorship.food_delivery_app.auth.service.contract;

import com.mentorship.food_delivery_app.auth.dto.OtpRequestDto;
import com.mentorship.food_delivery_app.auth.dto.OtpVerificationRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface OtpService {

    void requestOtp(OtpRequestDto otpRequestDto);

    UserDetails verifyOtp(OtpVerificationRequestDto otpRequest);
}
