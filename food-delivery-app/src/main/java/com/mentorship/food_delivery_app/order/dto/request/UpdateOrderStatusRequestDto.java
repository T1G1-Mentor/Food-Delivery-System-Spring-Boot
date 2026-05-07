package com.mentorship.food_delivery_app.order.dto.request;

import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Order ID is required")
        UUID orderId,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        @NotNull(message = "Description is required")
        String description,

        @NotNull(message = "Order status is required")
        OrderStatus status
) {

}
