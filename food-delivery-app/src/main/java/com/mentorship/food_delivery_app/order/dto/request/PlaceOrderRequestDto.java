package com.mentorship.food_delivery_app.order.dto.request;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.payment.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PlaceOrderRequestDto(
        @NotNull
        DeliveryAddressDto deliveryAddress,
        UUID couponId,
        @NotNull
        UUID restaurantBranchId,
        @NotNull
        List<CartItemRequestDto> cartItems,
        @NotNull
        PaymentMethod paymentMethod,
        @NotNull
        UUID cartId,
        String orderNotes
) {
}
