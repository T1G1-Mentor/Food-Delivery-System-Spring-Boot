package com.mentorship.food_delivery_app.cart.mapper;

import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartResponseDto toResponse(Cart cart) {
        Set<CartItemResponseDto> cartItemResponses = cart.getCartItems().stream()
                .map(cartItemMapper::toResponse)
                .collect(Collectors.toSet());

        return new CartResponseDto(cart.calculateTotal(), cartItemResponses);
    }

    public CartResponseDto toResponse(Cart cart, Set<CartItem> cartItems) {
        Set<CartItemResponseDto> cartItemResponses = cartItems.stream()
                .map(cartItemMapper::toResponse)
                .collect(Collectors.toSet());

        return new CartResponseDto(cart.calculateTotal(), cartItemResponses);
    }
}
