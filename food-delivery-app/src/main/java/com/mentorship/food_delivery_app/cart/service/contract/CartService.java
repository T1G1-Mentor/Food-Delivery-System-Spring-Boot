package com.mentorship.food_delivery_app.cart.service.contract;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;

import java.util.UUID;

public interface CartService {
    /**
     * Customer must be logged in to be able to add items to cart.
     * if the Customer is new and does not yet have cart in the system
     * then we create new cart for him
     * if not we proceed with item addition.
     *
     * @param cartItemRequest the request dto with value of the new item to add to cart.
     */
    CartResponseDto addToCart(CartItemRequestDto cartItemRequest);

    CartResponseDto viewCartItems();

    /**
     * Increases Cart item quantity by one.
     *
     * @param cartItemId The UUID value of the cart item id.
     *
     */
    CartResponseDto increaseCartItemQuantity(UUID cartItemId);


    /**
     * Decreases Cart item quantity by one.
     *
     * @param cartItemId The UUID value of the cart item id.
     *
     */
    CartResponseDto decreaseCartItemQuantity(UUID cartItemId);


    /**
     * Permanently removes item from the cart.
     *
     * @param cartItemId The UUID value of the cart item id.
     *
     */
    void removeCartItem(UUID cartItemId);


    /**
     * checks for the parameter existence (should take only one cart)
     * if it does not exist we fetch the logged in customer data
     * if it does exist we don't fetch our logged in customer data.
     *
     * @param carts optional var args of the cart entity.
     *
     */
    void clearCart(Cart... carts);
}
