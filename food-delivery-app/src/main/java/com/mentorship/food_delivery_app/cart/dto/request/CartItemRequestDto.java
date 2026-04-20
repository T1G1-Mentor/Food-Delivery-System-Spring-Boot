package com.mentorship.food_delivery_app.cart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CartItemRequestDto(
        @NotNull(message = "Item id cannot be empty.")
        @NotBlank(message = "Item id cannot be empty.")
        UUID menuItemId,

        @NotNull
        @Positive(message = "Quantity cannot be less than 1.")
        Integer quantity,

        @Size(max = 255, message = "Item note must be from 0 to 255")
        String note
) {
}
