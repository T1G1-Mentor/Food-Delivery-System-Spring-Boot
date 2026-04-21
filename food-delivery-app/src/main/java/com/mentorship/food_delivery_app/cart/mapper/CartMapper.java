package com.mentorship.food_delivery_app.cart.mapper;

import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponseDto toResponse(Cart cart){
        Set<CartItemResponseDto> cartItemResponses=cart.getCartItems()
                .stream().map(
                        item->
                                new CartItemResponseDto(
                                        item.getMenuItem().getId(),
                                        item.getQuantity(),
                                        item.getNote(),
                                        item.getMenuItem().getName(),
                                        item.getMenuItem().getDescription(),
                                        item.getMenuItem()
                                                .getPrice()
                                                .multiply(BigDecimal.valueOf(item.getQuantity()))))
                .collect(Collectors.toSet());

        return new CartResponseDto(
                cartItemResponses.stream()
                        .map(CartItemResponseDto::subTotal)
                        .reduce(BigDecimal.ZERO,BigDecimal::add),
                cartItemResponses
        );
    }
}
