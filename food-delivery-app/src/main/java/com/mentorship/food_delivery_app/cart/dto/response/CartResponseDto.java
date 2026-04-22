package com.mentorship.food_delivery_app.cart.dto.response;

import com.mentorship.food_delivery_app.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record CartResponseDto(
        BigDecimal total,
        Set<CartItemResponseDto> cartItems
) {
    public static CartResponseDto fromCart(Cart userCart) {
        return new CartResponseDto(
                userCart.calculateTotal(),
                userCart.getCartItems().stream()
                        .map(CartItemResponseDto::fromCartItem)
                        .collect(Collectors.toSet())
        );
    }

    public static CartResponseDto emptyCart() {
        return new CartResponseDto(
                BigDecimal.ZERO,
                Set.of()
        );
    }
}
