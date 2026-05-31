package com.mentorship.food_delivery_app.order.dto.response;

import com.mentorship.food_delivery_app.order.enums.OrderStatus;

import java.time.Instant;

public record OrderTrackingDto(
        OrderStatus status,
        String description,
        Instant createdAt
) {
}
