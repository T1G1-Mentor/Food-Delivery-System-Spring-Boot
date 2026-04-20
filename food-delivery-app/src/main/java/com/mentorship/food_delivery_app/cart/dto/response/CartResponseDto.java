package com.mentorship.food_delivery_app.cart.dto.response;

import java.math.BigDecimal;
import java.util.Set;

public record CartResponseDto(
        BigDecimal total,
        Set<CartItemResponseDto> cartItems
) {
}
