package com.mentorship.food_delivery_app.restaurant.dto.menuitem.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMenuItemRequestDto(
        @NotNull
        UUID menuItemId,
        @Size(max = 50, min = 5, message = "Menu item name must be from 5 to 50 characters.")
        String menuItemName,
        @Size(max = 250, min = 5, message = "Menu item name must be from 5 to 250 characters.")
        String menuItemDescription,
        @Positive(message = "Menu item price must be greater than zero")
        BigDecimal menuItemPrice
) {
}
