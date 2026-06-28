package com.mentorship.food_delivery_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpVerificationRequestDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email")
        String userEmail,
        @NotBlank(message = "OTP is required")
        @Size(max = 6, min = 6, message = "OTP code must be 6 characters")
        String otpCode
) {
}
