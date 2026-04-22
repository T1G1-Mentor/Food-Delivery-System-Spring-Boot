package com.mentorship.food_delivery_app.cart.dto.response;

import com.mentorship.food_delivery_app.cart.entity.CartItem;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponseDto(
        UUID menuItemId,
        Integer quantity,
        String note,
        String menuItemName,
        String menuItemDescription,
        BigDecimal subTotal
) {

    public static CartItemResponseDto fromCartItem(CartItem cartItem) {
        return new CartItemResponseDto(
                cartItem.getMenuItem().getId(),
                cartItem.getQuantity(),
                cartItem.getNote(),
                cartItem.getMenuItem().getName(),
                cartItem.getMenuItem().getDescription(),
                cartItem.getTotalPrice()
        );
    }
}
