package com.mentorship.food_delivery_app.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 50, message = "Email cannot exceed 50 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(max = 64, message = "Password exceeds maximum allowed length.")
        String password
) {
}
