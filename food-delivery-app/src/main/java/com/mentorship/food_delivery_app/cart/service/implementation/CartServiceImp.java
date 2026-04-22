package com.mentorship.food_delivery_app.cart.service.implementation;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.exceptions.CartItemNotFoundException;
import com.mentorship.food_delivery_app.user.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Value("${app.test.user-id}")
    private String userId;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CustomerService customerService;

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
    public CartResponseDto increaseCartItemQuantity(UUID itemId) {
        return null;
    }

    @Override
    public CartResponseDto decreaseCartItemQuantity(UUID itemId) {
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

    @Transactional
    @Override
    public CartResponseDto removeCartItem(UUID menuItemId) {
        Customer customer = customerService.
                fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));
        Cart cart = customer.getCart();

        if (cart == null)
            throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND_TO_REMOVE_FROM.getMessage());

        Optional<CartItem> existingItem = cart.
                getCartItems().
                stream().
                filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst();

        if (existingItem.isEmpty())
            throw new ResourceNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage());

        cart.getCartItems().remove(existingItem.get());

        return cartMapper.toResponse(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        if (cart==null)
            throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());
        cartItemRepository.deleteCartItemsByCartId(cart.getId());
        cart.setLocked(false);
        cart.setCurrentRestaurant(null);
    }

    @Transactional
    @Override
    public void clearLoggedInCustomerCart() {
        Customer customer=customerService.fetchCustomerWithCartOnlyByUserId(UUID.fromString(userId));
        clearCart(customer.getCart());
    }
}
