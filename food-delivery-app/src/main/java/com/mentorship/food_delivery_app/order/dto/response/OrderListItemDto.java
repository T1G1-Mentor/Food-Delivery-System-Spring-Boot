package com.mentorship.food_delivery_app.order.dto.response;

import com.mentorship.food_delivery_app.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderListItemDto(
        UUID orderId,
        OrderStatus status,
        Instant orderDate,
        BigDecimal total,
        String customerFullName,
        String restaurantName
) {
}
