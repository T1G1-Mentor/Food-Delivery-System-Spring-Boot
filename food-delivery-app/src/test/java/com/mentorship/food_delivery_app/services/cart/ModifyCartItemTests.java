package com.mentorship.food_delivery_app.services.cart;


import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.mapper.CartMapper;
import com.mentorship.food_delivery_app.cart.repository.CartItemRepository;
import com.mentorship.food_delivery_app.cart.repository.CartRepository;
import com.mentorship.food_delivery_app.cart.service.implementation.CartServiceImp;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.cart.exceptions.CartItemNotFoundException;
import com.mentorship.food_delivery_app.cart.exceptions.CartNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModifyCartItemTests {

    @Mock
    private CustomerService customerService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImp cartService;

    // --- shared test data ---
    private UUID menuItemId;
    private UUID cartId;
    private Cart cart;
    private CartItem cartItem;

    private Set<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        cartId = UUID.randomUUID();
        MenuItem menuItem;
        menuItem = new MenuItem();
        menuItem.setMenuItemId(menuItemId);
        menuItem.setName("Burger");
        menuItem.setDescription("Tasty beef burger");
        menuItem.setPrice(BigDecimal.valueOf(10.00));

        cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(1);
        cartItem.setNote("No onions");
        cartItems = Set.of(cartItem);

        cart = new Cart();
        cart.setCartId(cartId);
        cart.setCartItems(Set.of(cartItem));
    }

    // ------------------------------------------------------------------ //
    //  Happy-path tests                                                    //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("Successful modification")
    class SuccessScenarios {
        @BeforeEach
        void assumptions() {
            when(cartRepository.findByCustomerId(any()))
                    .thenReturn(Optional.of(cart));
            when(cartItemRepository.findAllByCartId(cartId))
                    .thenReturn(cartItems);
            when(cartMapper.toResponse(cart, cartItems)).thenReturn(buildCartResponse());

        }

        @AfterEach
        void verifications() {
            verify(cartMapper, times(1))
                    .toResponse(cart, cartItems);
        }

        @Test
        @DisplayName("Updates quantity when a valid positive quantity is provided")
        void shouldUpdateQuantity_whenValidQuantityProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(3, null);


            CartResponseDto result = cartService.modifyCartItem(any(), menuItemId, request);

            assertThat(cartItem.getQuantity()).isEqualTo(3);
            assertThat(result).isEqualTo(buildCartResponse());

        }

        @Test
        @DisplayName("Updates note when a non-null note is provided")
        void shouldUpdateNote_whenNoteProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(null, "Extra spicy");


            cartService.modifyCartItem(any(), menuItemId, request);

            assertThat(cartItem.getNote()).isEqualTo("Extra spicy");

        }

        @Test
        @DisplayName("Updates both quantity and note when both are provided")
        void shouldUpdateBothFields_whenBothProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(5, "Well done");


            cartService.modifyCartItem(any(), menuItemId, request);

            assertThat(cartItem.getQuantity()).isEqualTo(5);
            assertThat(cartItem.getNote()).isEqualTo("Well done");

        }

        @Test
        @DisplayName("Clears note when an empty string is provided")
        void shouldClearNote_whenEmptyStringProvided() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(null, "");

            cartService.modifyCartItem(any(), menuItemId, request);

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

        @AfterEach
        void verifications() {
            verifyNoInteractions(cartMapper);
        }

        @Test
        @DisplayName("Throws CartNotFoundException when customer has no cart")
        void shouldThrowException_whenCartIsNull() {
            CartItemModifyRequestDto request =
                    new CartItemModifyRequestDto(1, null);

            when(cartRepository.findByCustomerId(any()))
                    .thenReturn(Optional.empty());

            assertThrows(CartNotFoundException.class,
                    ()->cartService.modifyCartItem(any(), menuItemId, request));

        }

        @Test
        @DisplayName("Throws CartItemNotFoundException when cart item is not found")
        void shouldThrowException_whenCartItemNotFound() {
            CartItemModifyRequestDto request
                    = new CartItemModifyRequestDto(1, null);

            when(cartRepository.findByCustomerId(any()))
                    .thenReturn(Optional.of(cart));
            when(cartItemRepository.findAllByCartId(any()))
                    .thenReturn(Set.of());

            assertThatThrownBy(() -> cartService.modifyCartItem(any(), menuItemId, request))
                    .isInstanceOf(CartItemNotFoundException.class);

        }


    }

    // ------------------------------------------------------------------ //
    //  Interaction / delegation tests                                      //
    // ------------------------------------------------------------------ //
    @Nested
    @DisplayName("Collaboration verifications")
    class CollaborationTests {

        @Test
        @DisplayName("Fetches the cart items using the correct cartId")
        void shouldFetchCartItem_withCorrectIds() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(2, null);

            when(cartRepository.findByCustomerId(any()))
                    .thenReturn(Optional.of(cart));
            when(cartItemRepository.findAllByCartId(cartId))
                    .thenReturn(cartItems);
            when(cartMapper.toResponse(cart, cartItems)).
                    thenReturn(buildCartResponse());

            cartService.modifyCartItem(any(), menuItemId, request);

            verify(cartItemRepository, times(1))
                    .findAllByCartId(cartId);
        }

        @Test
        @DisplayName("Calls cartMapper exactly once with the cart")
        void shouldCallMapper_exactlyOnce() {
            CartItemModifyRequestDto request = new CartItemModifyRequestDto(2, null);

            when(cartRepository.findByCustomerId(any()))
                    .thenReturn(Optional.of(cart));
            when(cartItemRepository.findAllByCartId(cartId))
                    .thenReturn(cartItems);

            cartService.modifyCartItem(any(), menuItemId, request);

            verify(cartMapper, times(1))
                    .toResponse(cart, cartItems);
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
