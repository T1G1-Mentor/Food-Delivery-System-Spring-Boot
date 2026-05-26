package com.mentorship.food_delivery_app.order.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlaceOrderRequestDto(
        @NotNull
        DeliveryAddressDto deliveryAddress,
        UUID couponId
) {
}
