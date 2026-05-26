package com.mentorship.food_delivery_app.order.dto.response;

import com.mentorship.food_delivery_app.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDto(
        UUID orderId,
        OrderStatus status,
        Instant orderDate,
        BigDecimal subtotal,
        BigDecimal fee,
        BigDecimal discountValue,
        BigDecimal total,
        String note,
        String customerFullName,
        String restaurantName,
        AddressDto deliveryAddress,
        BigDecimal couponAmount,
        List<OrderItemDto> items,
        List<OrderTrackingDto> trackingHistory
) {
}
