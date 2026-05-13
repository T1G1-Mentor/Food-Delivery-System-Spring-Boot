package com.mentorship.food_delivery_app.order.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID orderItemId,
        String menuItemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal,
        String note
) {
}
