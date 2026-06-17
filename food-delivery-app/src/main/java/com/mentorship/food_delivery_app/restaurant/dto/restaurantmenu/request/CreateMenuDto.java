package com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMenuDto(
        @NotBlank(message = "Menu name cannot be empty.")
        @Size(min = 5, max = 30, message = "Menu name must be from 5 to 30 character long.")
        String restaurantMenuName
) {
}
