package com.mentorship.food_delivery_app.restaurant.dto.menuitem.request;

import java.math.BigDecimal;

public record MenuItemRequestDto(
        String menuItemName,
        String menuItemDescription,
        BigDecimal menuItemPrice
) {
}
