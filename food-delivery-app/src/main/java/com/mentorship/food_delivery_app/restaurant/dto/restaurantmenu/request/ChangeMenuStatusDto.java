package com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request;

import jakarta.validation.constraints.NotNull;

public record ChangeMenuStatusDto(
        @NotNull(message = "Menu status cannot be null")
        Boolean isEnabled
) {
}
