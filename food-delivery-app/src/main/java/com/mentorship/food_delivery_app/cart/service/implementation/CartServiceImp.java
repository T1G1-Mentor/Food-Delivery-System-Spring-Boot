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
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImp implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    @Value("${app.test.user-id}")
    private String userId;


    @Transactional
    @Override
    public CartResponseDto addToCart(CartItemRequestDto cartItemRequest) {
        Customer customer = customerService.fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));

        Cart cart = getOrCreateCustomerCart(customer);

        MenuItem menuItem = restaurantService.getMenuItemById(cartItemRequest.menuItemId());
        validateCartCurrentRestaurant(cart.getCurrentRestaurant(), menuItem.getRestaurantBranch().getId());

        Optional<CartItem> existingItem =cart.searchExistingItem(cartItemRequest.menuItemId());

        existingItem.
                ifPresentOrElse(item->
                                item.setQuantity(cartItemRequest.quantity()
                                ),
                ()->
                        validateAndCreateNewCartItem(cart,cartItemRequest,menuItem
                        ));


        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponseDto viewCartItems() {
        Cart cart = customerService.fetchCustomerWithCartInfoByUserId(UUID.fromString(userId)).getCart();
        if (cart == null) return CartResponseDto.emptyCart();
        return cartMapper.toResponse(cart);
    }

    @Transactional
    @Override
    public CartResponseDto modifyCartItem(UUID menuItemId, CartItemModifyRequestDto cartItemRequest) {
        log.info("Modifying cart item with menu item id {} for user id {}", menuItemId, userId);
        Cart cart = validateAndGetLoggedInCustomerCart();

        CartItem cartItem = cartItemRepository.findByMenuItemIdAndCart(menuItemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

        // If the cart item has a lot of fields, we can create a command class to encapsulate the modifications and arguments.
        cartItem.applyModifications(cartItemRequest.quantity(), cartItemRequest.note());

        return cartMapper.toResponse(cart);
    }

    @Transactional
    @Override
    public CartResponseDto removeCartItem(UUID menuItemId) {
        Cart cart = validateAndGetLoggedInCustomerCart();

        log.info("Removing item from cart with id {}", cart.getId());

        CartItem item  = cart.searchExistingItem(menuItemId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

        log.info("Removing item from cart: menu item id {}, cart id {}", item.getMenuItem().getId(), cart.getId());

        cart.removeCartItem(item);

        return cartMapper.toResponse(cart);
    }

    //    Transactional annotation required (the method will be called from order domain)
    @Transactional
    @Override
    public void clearCart(Cart cart) {
        if (cart == null)
            throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());

        log.info("Clearing cart with id {}", cart.getId());
        cartItemRepository.deleteCartItemsByCartId(cart.getId());
        cart.setLocked(false);
        cart.setCurrentRestaurant(null);
    }

    @Transactional
    @Override
    public void clearLoggedInCustomerCart() {
        Customer customer = customerService.fetchCustomerWithCartOnlyByUserId(UUID.fromString(userId));
        clearCart(customer.getCart());
    }

    @Override
    public void lockCart(UUID cartId) {
        log.info("Locking cart with id {}", cartId);
        cartRepository.findByIdWithLock(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage()));
    }

    private Cart validateAndGetLoggedInCustomerCart() {
        Cart cart = customerService.
                fetchCustomerWithCartInfoByUserId(UUID.fromString(userId)).
                getCart();

        if (cart == null)
            throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());

        return cart;
    }

    private void validateCartCurrentRestaurant(RestaurantBranch currentRestaurant, UUID menuItemRestaurantBranchId) {

        if (currentRestaurant != null && !menuItemRestaurantBranchId.equals(currentRestaurant.getId()))
            throw new BadRequestException(ErrorMessage.ITEM_DIFFERENT_RESTAURANT.getMessage());

    }


    private Cart getOrCreateCustomerCart(Customer customer) {
        Cart cart = customer.getCart();
        if (cart != null)
            return cart;

        cart = Cart.builder()
                .customer(customer)
                .isLocked(false)
                .cartItems(new HashSet<>())
                .build();
        customer.setCart(cart);
        return cartRepository.save(cart);

    }

    private void validateAndCreateNewCartItem(Cart cart, CartItemRequestDto cartItemRequest, MenuItem menuItem) {
        if (!menuItem.isAvailable())
            throw new BadRequestException(ErrorMessage.MENU_ITEM_NOT_AVAILABLE.getMessage());
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .menuItem(menuItem)
                .quantity(cartItemRequest.quantity())
                .note(cartItemRequest.note())
                .build();
        cart.getCartItems().add(newItem);

        if (cart.getCurrentRestaurant() == null) {
            cart.setCurrentRestaurant(menuItem.getRestaurantBranch());
        }
    }
}
