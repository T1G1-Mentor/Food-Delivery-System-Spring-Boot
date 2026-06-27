package com.mentorship.food_delivery_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpRequestDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email")
        String userEmail
) {
}
