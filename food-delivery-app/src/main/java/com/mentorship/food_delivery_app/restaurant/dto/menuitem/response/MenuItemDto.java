package com.mentorship.food_delivery_app.restaurant.dto.menuitem.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemDto (
        UUID menuItemId,
        String menuItemName,
        String menuItemDescription,
        BigDecimal menuItemPrice
){
}
