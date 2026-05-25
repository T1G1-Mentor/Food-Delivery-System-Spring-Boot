package com.mentorship.food_delivery_app.services.cart;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.implementation.CartServiceImp;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.user.exceptions.CartItemNotFoundException;
import com.mentorship.food_delivery_app.user.exceptions.CartNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
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
    @InjectMocks
    private CartServiceImp cartService;


    @BeforeEach
    void beforeEach() {
        when(customerService.getLoggedinCustomer())
                .thenReturn(new Customer());
    }

    @Test
    @DisplayName("""
            GIVEN: customer does not have a cart
            WHEN: when remove item from cart is called
            THEN: an exception is thrown
            AND: cart item repository is never called
            """)
    void test_1() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();

        when(cartRepository.findByCustomerId(any()))
                .thenReturn(Optional.empty());

//        actual method call and assertions
        assertThrows(CartNotFoundException.class,
                () -> cartService.removeCartItem(menuItemId));

        verifyNoInteractions(cartItemRepository);
    }

    @Test
    @DisplayName("""
            GIVEN: customer have a cart
            AND: menu item does not exists in the cart
            WHEN: when remove item from cart is called
            THEN: an exception is thrown
            AND: cartItemRepository.delete will never be called
            """)
    void test_2() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();


        when(cartRepository.findByCustomerId(any()))
                .thenReturn(Optional.of(new Cart()));
        when(cartItemRepository.findByMenuItemIdAndCart(any(), any()))
                .thenReturn(Optional.empty());

//        actual method call & assertions
        assertThrows(CartItemNotFoundException.class,
                () -> cartService.removeCartItem(menuItemId));

        verify(cartItemRepository, never())
                .delete(any());

    }


    @Test
    @DisplayName("""
            GIVEN: customer have a cart
            AND: menu item exists in the cart
            WHEN: when remove item from cart is called
            THEN: cart item repository will be called to delete item
            """)
    void test_3() {

//        assumptions
        UUID menuItemId = UUID.randomUUID();


        when(cartRepository.findByCustomerId(any()))
                .thenReturn(Optional.of(new Cart()));

        when(cartItemRepository.findByMenuItemIdAndCart(any(), any()))
                .thenReturn(Optional.of(new CartItem()));
//        actual method call
        cartService.removeCartItem(menuItemId);


//        verifications
        verify(cartItemRepository, times(1))
                .delete(any());
    }

}
