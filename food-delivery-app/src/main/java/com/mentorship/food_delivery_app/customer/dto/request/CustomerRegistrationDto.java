package com.mentorship.food_delivery_app.customer.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CustomerRegistrationDto(

        @NotBlank(message = "First name is required.")
        @Size(max = 50, message = "First name cannot exceed 50 characters.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(max = 50, message = "Last name cannot exceed 50 characters.")
        String lastName,

        @NotNull(message = "Birth date is required.")
        @Past(message = "Birth date must be in the past.")
        LocalDate birthDate,

        @NotBlank(message = "Phone number is required.")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$",
                message = "Invalid phone number format.")
        String phone,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 50, message = "Email cannot exceed 50 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
        String password
) {
}
