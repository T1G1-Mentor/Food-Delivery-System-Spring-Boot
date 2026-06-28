package com.mentorship.food_delivery_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpSendRequest(
        @NotBlank @Email @Size(max = 50) String email
) {}
