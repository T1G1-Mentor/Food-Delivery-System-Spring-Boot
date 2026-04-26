package com.mentorship.food_delivery_app.services.cart;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.implementation.CartServiceImp;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RemoveCartItemTests {
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private MenuItemRepository menuItemRepository;
    @InjectMocks
    private CartServiceImp cartService;

    private UUID userId;

    @BeforeEach
    public void beforeEach() {
        userId = UUID.randomUUID();
        ReflectionTestUtils.setField(cartService,
                "userId", userId.toString());
    }

    @Test
    @DisplayName("""
            GIVEN: customer does not have a cart
            WHEN: when remove item from cart is called
            THEN: an exception is thrown
            """)
    void test_1() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();

        when(customerService.fetchCustomerWithCartInfoByUserId(userId))
                .thenReturn(new Customer());

//        actual method call and assertions
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeCartItem(menuItemId));

//        verifications
        verify(cartMapper, never()).toResponse(new Cart());
    }

    @Test
    @DisplayName("""
            GIVEN: customer have a cart
            AND: menu item does not exists in the cart
            WHEN: when remove item from cart is called
            THEN: an exception is thrown
            """)
    void test_2() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();
        Customer customer = getCustomerWithEmptyCart();

        when(customerService.fetchCustomerWithCartInfoByUserId(userId))
                .thenReturn(customer);

//        actual method call & assertions
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeCartItem(menuItemId));

//        verifications
        verify(cartMapper, never()).toResponse(new Cart());
    }


    @Test
    @DisplayName("""
            GIVEN: customer have a cart
            AND: menu item exists in the cart
            WHEN: when remove item from cart is called
            THEN: cart mapper will be called once
            """)
    void test_3() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();
        Customer customer = getCustomerWithItemExistsInCart(menuItemId);

        when(customerService.fetchCustomerWithCartInfoByUserId(userId))
                .thenReturn(customer);

//        actual method call
        cartService.removeCartItem(menuItemId);


//        verifications
        verify(cartMapper, times(1))
                .toResponse(customer.getCart());
    }

    private Customer getCustomerWithEmptyCart() {
        Cart cart = Cart.builder()
                .id(UUID.randomUUID()).
                isLocked(false)
                .currentRestaurant(null)
                .cartItems(new HashSet<>()).build();

        Customer customer = new Customer();

        customer.setCart(cart);
        cart.setCustomer(customer);

        return customer;
    }

    private Customer getCustomerWithItemExistsInCart(UUID menuItemId) {
        Customer customer = getCustomerWithEmptyCart();
        customer.
                getCart().
                getCartItems().
                add(
                        CartItem.builder()
                                .id(1L)
                                .quantity(1)
                                .note("")
                                .cart(customer.getCart())
                                .menuItem(
                                        MenuItem.builder()
                                                .id(menuItemId)
                                                .description("")
                                                .name("")
                                                .price(BigDecimal.ONE)
                                                .isAvailable(true)
                                                .build()
                                ).build());

        return customer;
    }
}
