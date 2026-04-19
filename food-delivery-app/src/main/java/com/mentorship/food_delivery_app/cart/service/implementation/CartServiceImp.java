package com.mentorship.food_delivery_app.cart.service.implementation;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CartServiceImp implements CartService {
    @Override
    public CartResponseDto addToCart(CartItemRequestDto cartItemRequest) {
        return null;
    }

    @Override
    public CartResponseDto viewCartItems() {
        return null;
    }

    @Override
    public CartResponseDto increaseCartItemQuantity(UUID cartItemId) {
        return null;
    }

    @Override
    public CartResponseDto decreaseCartItemQuantity(UUID cartItemId) {
        return null;
    }

    @Override
    public void removeCartItem(UUID cartItemId) {

    }

    @Override
    public void clearCart(Cart... carts) {

    }
}
