package com.mentorship.food_delivery_app.cart.service.contract;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
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

    CartResponseDto modifyCartItem(UUID menuItemId, CartItemModifyRequestDto cartItemRequest);


    /**
     * Permanently removes item from the cart.
     *
     * @param menuItemId The UUID value of the cart item id.
     *
     */
    CartResponseDto removeCartItem(UUID menuItemId);


    /**
     * Clears the cart immediately and sets {@code isLocked} to {@code false}
     * @param cart cart entity to be cleared.
     *
     */
    void clearCart(Cart cart);

    /**
     * Calls method the fetches the logged in customer by user id along with his cart.
     * clears the cart immediately and sets {@code isLocked} to {@code false}
     * */
    void clearLoggedInCustomerCart();

    void lockCart(UUID cartId);
}
