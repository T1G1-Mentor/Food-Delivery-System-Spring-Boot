package com.mentorship.food_delivery_app.cart.service.implementation;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.exceptions.CartLockedException;
import com.mentorship.food_delivery_app.cart.exceptions.ItemNotAvailableException;
import com.mentorship.food_delivery_app.cart.exceptions.RestaurantMismatchException;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import com.mentorship.food_delivery_app.user.exceptions.CartItemNotFoundException;
import com.mentorship.food_delivery_app.user.exceptions.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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


    @Transactional
    @Override
    public CartResponseDto addToCart(CartItemRequestDto cartItemRequest) {

        Cart cart = getOrCreateLoggedinCustomerCart();
        if (cart.isLocked())
            throw new CartLockedException();

        MenuItem menuItem = restaurantService.getMenuItemById(cartItemRequest.menuItemId());
        validateCartCurrentRestaurant(cart.getCurrentRestaurant(), menuItem.getRestaurantBranch().getId());

        Set<CartItem> cartItems = this.getCartItemsWithMenuItemsByCartId(cart.getId());
        Optional<CartItem> existingItem = searchExistingItem(cartItems, menuItem.getId());


        existingItem.
                ifPresentOrElse(item ->
                                item.setQuantity(cartItemRequest.quantity()
                                ),
                        () ->
                                validateAndCreateNewCartItem(cart, cartItemRequest, menuItem
                                ));


        return cartMapper.toResponse(cart);
    }


    @Override
    public CartResponseDto viewCartItems() {
        Customer customer = customerService.getLoggedinCustomer();

        Optional<Cart> cart = cartRepository.findWithCartItemsAndMenuItemsByCustomerId(customer.getId());

        if (cart.isEmpty()) return CartResponseDto.emptyCart();

        return cartMapper.toResponse(cart.get());
    }

    @Transactional
    @Override
    public CartResponseDto modifyCartItem(UUID menuItemId, CartItemModifyRequestDto cartItemRequest) {
        log.info("Modifying cart item with menu item id {}", menuItemId);
        Cart cart = validateAndGetLoggedInCustomerCart();

        Set<CartItem> cartItems = this.getCartItemsWithMenuItemsByCartId(cart.getId());

        CartItem cartItem = searchExistingItem(cartItems, menuItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

        // If the cart item has a lot of fields, we can create a command class to encapsulate the modifications and arguments.
        cartItem.applyModifications(cartItemRequest.quantity(), cartItemRequest.note());

        return cartMapper.toResponse(cart, cartItems);
    }

    @Transactional
    @Override
    public void removeCartItem(UUID menuItemId) {
        Cart cart = validateAndGetLoggedInCustomerCart();

        log.info("Removing item from cart with id {}", cart.getId());
        CartItem cartItem = cartItemRepository.
                findByMenuItemIdAndCart(menuItemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

        log.info("Removing item from cart: menu item id {}, cart id {}", menuItemId, cart.getId());
        cartItemRepository.delete(cartItem);
    }

    //    Transactional annotation required (the method will be called from order domain)
    @Transactional
    @Override
    public void clearCart(Cart cart) {
        if (cart == null)
            throw new CartNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());

        log.info("Clearing cart with id {}", cart.getId());
        cartItemRepository.deleteCartItemsByCartId(cart.getId());
        cart.setLocked(false);
        cart.setCurrentRestaurant(null);
    }

    @Transactional
    @Override
    public void clearLoggedInCustomerCart() {
        Cart cart = validateAndGetLoggedInCustomerCart();
        clearCart(cart);
    }

    @Override
    public void lockCart(UUID cartId) {
        log.info("Locking cart with id {}", cartId);
        cartRepository.findByIdWithLock(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage()));
    }

    public Cart getCartByCustomerId(UUID customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    log.error("Cart not found for customer {}", customerId);
                    return new CartNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());
                });
    }

    public Set<CartItem> getCartItemsWithMenuItemsByCartId(UUID cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }

    private Cart validateAndGetLoggedInCustomerCart() {
        Customer customer = customerService.
                getLoggedinCustomer();
        return cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage()));
    }

    private void validateCartCurrentRestaurant(RestaurantBranch currentRestaurant, UUID menuItemRestaurantBranchId) {

        if (currentRestaurant != null && !menuItemRestaurantBranchId.equals(currentRestaurant.getId()))
            throw new RestaurantMismatchException(ErrorMessage.ITEM_DIFFERENT_RESTAURANT.getMessage());

    }


    private Cart getOrCreateLoggedinCustomerCart() {
        Customer customer = customerService.getLoggedinCustomer();

        return
                cartRepository.findByCustomerId(customer.getId())
                        .orElseGet(() -> {
                            Cart newCart = Cart.builder()
                                    .customer(customer)
                                    .isLocked(false)
                                    .cartItems(new HashSet<>())
                                    .build();
                            return cartRepository.save(newCart);
                        });
    }

    private void validateAndCreateNewCartItem(Cart cart, CartItemRequestDto cartItemRequest, MenuItem menuItem) {
        if (!menuItem.isAvailable())
            throw new ItemNotAvailableException(ErrorMessage.MENU_ITEM_NOT_AVAILABLE.getMessage());
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

    private Optional<CartItem> searchExistingItem(Set<CartItem> cartItems, UUID menuItemId) {
        return cartItems
                .stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst();
    }
}
