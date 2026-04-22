package com.mentorship.food_delivery_app.cart.service.implementation;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.exceptions.CartItemNotFoundException;
import com.mentorship.food_delivery_app.user.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CartServiceImp implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartResponseDto addToCart(CartItemRequestDto cartItemRequest) {
        return null;
    }

    @Override
    public CartResponseDto viewCartItems() {
        User currentUser = new User();
        Optional<Cart> userCart = cartRepository.findByCustomerId(currentUser.getId());
        return userCart.map(CartResponseDto::fromCart).orElseGet(CartResponseDto::emptyCart);
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
    public CartResponseDto modifyCartItem(CartItemModifyRequestDto cartItemRequest) {
        // 1- get the user cart
        User currentUser = new User();
        Cart userCart = cartRepository.findByCustomerId(currentUser.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user with id: " + currentUser.getId()));

        // 2- Check if the item exists in the cart or not
        CartItem cartItem = cartItemRepository.findByIdAndCart(cartItemRequest.cartItemId(), userCart)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemRequest.cartItemId()));

        // 3- update the quantity and note of the item
        if (cartItemRequest.quantity() != null) {
            cartItem.setQuantity(cartItemRequest.quantity());
        }

        if (cartItemRequest.note() != null) {
            cartItem.setNote(cartItemRequest.note());
        }

        cartItemRepository.save(cartItem);

        return CartResponseDto.fromCart(userCart);
    }

    @Override
    public void removeCartItem(UUID cartItemId) {

    }

    @Override
    public void clearCart(Cart cart) {

    }

    @Override
    public void clearLoggedInCustomerCart() {

    }
}
