package com.mentorship.food_delivery_app.cart.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDto(
        UUID itemId,
        Integer quantity,
        String note,
        String menuItemName,
        String menuItemDescription,
        BigDecimal subTotal
) {
}
