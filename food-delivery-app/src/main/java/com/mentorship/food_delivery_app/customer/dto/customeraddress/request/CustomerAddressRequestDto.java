package com.mentorship.food_delivery_app.customer.dto.customeraddress.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerAddressRequestDto(

        @NotNull(message = "Label cannot be null")
        @NotBlank(message = "Label cannot be empty")
        @Size(max = 20, message = "Label exceeds the limit")
        String label,

        @NotNull(message = "City cannot be null")
        @NotBlank(message = "City cannot be empty")
        @Size(max = 20, message = "City exceeds the limit")
        String city,

        @NotNull(message = "Street cannot be null")
        @NotBlank(message = "Street cannot be empty")
        @Size(max = 20, message = "Street exceeds the limit")
        String street,

        @NotNull(message = "Building cannot be null")
        @NotBlank(message = "Building cannot be empty")
        @Size(max = 20, message = "Building exceeds the limit")
        String building,

        @NotNull(message = "Apartment cannot be null")
        @NotBlank(message = "Apartment cannot be empty")
        @Size(max = 20, message = "Apartment exceeds the limit")
        String apartment,

        @NotNull(message = "Address Phone number cannot be null")
        @NotBlank(message = "Address Phone number cannot be empty")
        @Size(max = 15, min = 11, message = "Address Phone number must be between 11 and 15 character")
        String addressPhoneNumber,

        @Size(max = 500, message = "Address note must be between 0 and 500 character")
        String addressNote
) {
}
