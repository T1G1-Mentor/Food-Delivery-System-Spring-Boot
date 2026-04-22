package com.mentorship.food_delivery_app.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CartItemModifyRequestDto (
        @NotNull(message = "Item id must be provided.")
        UUID cartItemId,

        @Positive(message = "Quantity cannot be less than 1.")
        Integer quantity,

        @Size(max = 255, message = "Item note must be from 0 to 255 characters.")
        String note
) {
}
