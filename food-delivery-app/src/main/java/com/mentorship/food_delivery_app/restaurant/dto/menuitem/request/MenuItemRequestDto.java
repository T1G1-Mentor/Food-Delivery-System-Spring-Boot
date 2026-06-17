package com.mentorship.food_delivery_app.restaurant.dto.menuitem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequestDto(
        @NotBlank(message = "Menu item name cannot be empty.")
        @Size(max = 50, min = 5, message = "Menu item name must be from 5 to 50 characters.")
        String menuItemName,
        @NotBlank(message = "Menu item description cannot be empty.")
        @Size(max = 250, min = 5, message = "Menu item name must be from 5 to 250 characters.")
        String menuItemDescription,
        @Positive(message = "Menu item price must be greater than zero")
        BigDecimal menuItemPrice
) {
}
