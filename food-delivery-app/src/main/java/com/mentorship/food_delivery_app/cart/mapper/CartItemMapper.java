package com.mentorship.food_delivery_app.cart.mapper;

import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.entity.CartItem;

public class CartItemMapper {
    public CartItemResponseDto toResponse(CartItem cartItem) {
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
