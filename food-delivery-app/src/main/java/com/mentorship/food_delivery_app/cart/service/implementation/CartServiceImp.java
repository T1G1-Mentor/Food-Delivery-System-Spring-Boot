package com.mentorship.food_delivery_app.cart.service.implementation;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CartServiceImp implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CustomerService customerService;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    @Value("${app.test.user-id}")
    private String userId;



    @Override
    public CartResponseDto addToCart(CartItemRequestDto cartItemRequest) {
        Customer customer = customerService.fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));

        Cart cart = customer.getCart();
        if (cart == null) {
            cart = Cart.builder()
                    .customer(customer)
                    .isLocked(false)
                    .cartItems(new HashSet<>())
                    .build();
            cartRepository.save(cart);
            customer.setCart(cart);
        }

        MenuItem menuItem = menuItemRepository.findById(cartItemRequest.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ITEM_NOT_FOUND.getMessage()));

        if (cart.getCurrentRestaurant() != null) {
            UUID itemBranchId = menuItem.getMenu().getRestaurantBranch().getId();
            if (!itemBranchId.equals(cart.getCurrentRestaurant().getId())) {
                throw new BadRequestException(ErrorMessage.ITEM_DIFFERENT_RESTAURANT.getMessage());
            }
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(cartItemRequest.menuItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + cartItemRequest.quantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(cartItemRequest.quantity())
                    .note(cartItemRequest.note())
                    .build();
            cart.getCartItems().add(newItem);

            if (cart.getCurrentRestaurant() == null) {
                cart.setCurrentRestaurant(menuItem.getMenu().getRestaurantBranch());
            }
        }

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponseDto viewCartItems() {
        Customer customer = customerService.
                fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));
        Cart cart = customer.getCart();
        if (cart == null) {
            return CartResponseDto.emptyCart();
        }
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponseDto increaseCartItemQuantity(UUID itemId) {
        return null;
    }

    @Override
    public CartResponseDto decreaseCartItemQuantity(UUID itemId) {
        return null;
    }

    @Transactional
    @Override
    public CartResponseDto modifyCartItem(CartItemModifyRequestDto cartItemRequest) {
        // 1- get the user cart
        Customer customer = customerService.
                fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));
        Cart cart = customer.getCart();

        if (cart == null) throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND_TO_REMOVE_FROM.getMessage());

        // 2- Check if the item exists in the cart or not
        CartItem cartItem = cartItemRepository.findByIdAndCart(cartItemRequest.cartItemId(), cart)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

        // 3- update the quantity and note of the item
        if (cartItemRequest.quantity() != null) {
            cartItem.setQuantity(cartItemRequest.quantity());
        }

        if (cartItemRequest.note() != null) {
            cartItem.setNote(cartItemRequest.note());
        }

        cartItemRepository.save(cartItem);

        return cartMapper.toResponse(cart);
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
