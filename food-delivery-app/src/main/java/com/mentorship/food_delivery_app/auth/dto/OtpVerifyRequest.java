package com.mentorship.food_delivery_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OtpVerifyRequest(
        @NotBlank @Email @Size(max = 50) String email,
        @NotBlank @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit number") String code
) {}
