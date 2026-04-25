package com.mentorship.food_delivery_app.services.cart;


import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.cart.service.implementation.CartServiceImp;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModifyCartItemTests {

    @Mock
    private CustomerService customerService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImp cartService;

    // --- shared test data ---
    private UUID menuItemId;
    private UUID cartId;
    private Cart cart;
    private CartItem cartItem;
    private MenuItem menuItem;
    private Customer customer;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        cartId     = UUID.randomUUID();

        menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Burger");
        menuItem.setDescription("Tasty beef burger");
        menuItem.setPrice(BigDecimal.valueOf(10.00));

        cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(1);
        cartItem.setNote("No onions");

        cart = new Cart();
        cart.setId(cartId);
        cart.setCartItems(Set.of(cartItem));

        customer = new Customer();
        customer.setCart(cart);

        ReflectionTestUtils.setField(cartService, "userId", "019dac9d-de24-7d35-b386-2844f5d5bd84");
    }

    // ------------------------------------------------------------------ //
    //  Happy-path tests                                                    //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("Successful modification")
    class SuccessScenarios {

        @Test
        @DisplayName("Updates quantity when a valid positive quantity is provided")
        void shouldUpdateQuantity_whenValidQuantityProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(3, null);
            CartResponseDto expectedResponse = buildCartResponse();

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(expectedResponse);

            CartResponseDto result = cartService.modifyCartItem(menuItemId, request);

            assertThat(cartItem.getQuantity()).isEqualTo(3);
            assertThat(result).isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Updates note when a non-null note is provided")
        void shouldUpdateNote_whenNoteProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(null, "Extra spicy");

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(buildCartResponse());

            cartService.modifyCartItem(menuItemId, request);

            assertThat(cartItem.getNote()).isEqualTo("Extra spicy");
        }

        @Test
        @DisplayName("Updates both quantity and note when both are provided")
        void shouldUpdateBothFields_whenBothProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(5, "Well done");

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(buildCartResponse());

            cartService.modifyCartItem(menuItemId, request);

            assertThat(cartItem.getQuantity()).isEqualTo(5);
            assertThat(cartItem.getNote()).isEqualTo("Well done");
        }

        @Test
        @DisplayName("Clears note when an empty string is provided")
        void shouldClearNote_whenEmptyStringProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(null, "");

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(buildCartResponse());

            cartService.modifyCartItem(menuItemId, request);

            assertThat(cartItem.getNote()).isEmpty();
        }
    }

    // ------------------------------------------------------------------ //
    //  applyModifications guard-clause tests (unit-testing the entity)    //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("applyModifications edge cases")
    class ApplyModificationsEdgeCases {

        @Test
        @DisplayName("Does NOT update quantity when null is passed")
        void shouldNotUpdateQuantity_whenNullQuantity() {
            cartItem.setQuantity(2);
            cartItem.applyModifications(null, null);

            assertThat(cartItem.getQuantity()).isEqualTo(2); // unchanged
        }

        @Test
        @DisplayName("Does NOT update quantity when zero is passed")
        void shouldNotUpdateQuantity_whenZeroQuantity() {
            cartItem.setQuantity(2);
            cartItem.applyModifications(0, null);

            assertThat(cartItem.getQuantity()).isEqualTo(2); // unchanged
        }

        @Test
        @DisplayName("Does NOT update quantity when a negative value is passed")
        void shouldNotUpdateQuantity_whenNegativeQuantity() {
            cartItem.setQuantity(2);
            cartItem.applyModifications(-1, null);

            assertThat(cartItem.getQuantity()).isEqualTo(2); // unchanged
        }

        @Test
        @DisplayName("Does NOT update note when null is passed")
        void shouldNotUpdateNote_whenNullNote() {
            cartItem.setNote("Original note");
            cartItem.applyModifications(null, null);

            assertThat(cartItem.getNote()).isEqualTo("Original note"); // unchanged
        }
    }

    // ------------------------------------------------------------------ //
    //  Exception / sad-path tests                                         //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("Exception scenarios")
    class ExceptionScenarios {

        @Test
        @DisplayName("Throws ResourceNotFoundException when customer has no cart")
        void shouldThrowException_whenCartIsNull() {
            customer.setCart(null);
            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);

            CartItemModifyRequestDto request = new CartItemModifyRequestDto(1, null);

            assertThatThrownBy(() -> cartService.modifyCartItem(menuItemId, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Throws ResourceNotFoundException when cart item is not found")
        void shouldThrowException_whenCartItemNotFound() {
            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.empty());

            CartItemModifyRequestDto request = new CartItemModifyRequestDto(1, null);

            assertThatThrownBy(() -> cartService.modifyCartItem(menuItemId, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Does not call cartMapper when cart is null")
        void shouldNotCallMapper_whenCartIsNull() {
            customer.setCart(null);
            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);

            CartItemModifyRequestDto request = new CartItemModifyRequestDto(1, null);

            assertThatThrownBy(() -> cartService.modifyCartItem(menuItemId, request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verifyNoInteractions(cartMapper);
        }
    }

    // ------------------------------------------------------------------ //
    //  Interaction / delegation tests                                      //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("Collaboration verifications")
    class CollaborationTests {

        @Test
        @DisplayName("Fetches the cart item using the correct menuItemId and cartId")
        void shouldFetchCartItem_withCorrectIds() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(2, null);

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(buildCartResponse());

            cartService.modifyCartItem(menuItemId, request);

            verify(cartItemRepository).findByMenuItemIdAndCart(menuItemId, cartId);
        }

        @Test
        @DisplayName("Calls cartMapper exactly once with the cart")
        void shouldCallMapper_exactlyOnce() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(2, null);

            when(customerService.fetchCustomerWithCartInfoByUserId(any())).thenReturn(customer);
            when(cartItemRepository.findByMenuItemIdAndCart(menuItemId, cartId))
                    .thenReturn(Optional.of(cartItem));
            when(cartMapper.toResponse(cart)).thenReturn(buildCartResponse());

            cartService.modifyCartItem(menuItemId, request);

            verify(cartMapper, times(1)).toResponse(cart);
        }
    }

    // ------------------------------------------------------------------ //
    //  Helper                                                              //
    // ------------------------------------------------------------------ //
    private CartResponseDto buildCartResponse() {
        CartItemResponseDto itemDto = new CartItemResponseDto(
                menuItemId, 1, "No onions",
                "Burger", "Tasty beef burger",
                BigDecimal.valueOf(10.00)
        );
        return new CartResponseDto(BigDecimal.valueOf(10.00), Set.of(itemDto));
    }
}