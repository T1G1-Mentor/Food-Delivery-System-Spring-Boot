package com.mentorship.food_delivery_app.cart.controller;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller", description = "Customer cart management")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> viewCart(@AuthenticationPrincipal CustomerPrincipal customer) {
        return ResponseEntity.ok(
                cartService.viewCartItems(customer.getCustomerId())
        );
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody CartItemRequestDto cartItemRequest,
    @AuthenticationPrincipal CustomerPrincipal customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                cartService.addToCart(cartItemRequest, customer.getCustomerId())
        );
    }

    @PatchMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponseDto> modifyCartItem(@PathVariable UUID menuItemId,
                                                          @Valid @RequestBody CartItemModifyRequestDto request,
                                                          @AuthenticationPrincipal CustomerPrincipal customer) {
        CartResponseDto response = cartService.modifyCartItem(customer.getCustomerId(), menuItemId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponseDto> removeCartItem(@PathVariable UUID menuItemId,
                                                          @AuthenticationPrincipal CustomerPrincipal customer) {
        cartService.removeCartItem(customer.getCustomerId(), menuItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal CustomerPrincipal customer) {
        cartService.clearLoggedInCustomerCart(customer.getCustomerId());
        return ResponseEntity.noContent().build();
    }
}
