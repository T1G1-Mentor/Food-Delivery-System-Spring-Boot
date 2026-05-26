package com.mentorship.food_delivery_app.order.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
    UUID menuItemId,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal subTotal
) {
}
