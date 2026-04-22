package com.mentorship.food_delivery_app.cart.controller;

import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemModifyRequestDto;
import com.mentorship.food_delivery_app.cart.dto.request.CartItemRequestDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> viewCart(){
        return ResponseEntity.ok(
                cartService.viewCartItems()
        );
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody CartItemRequestDto cartItemRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                cartService.addToCart(cartItemRequest)
        );
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> modifyCartItem(CartItemModifyRequestDto request) {
        CartResponseDto response = cartService.modifyCartItem(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/cart-item/{menuItemId}")
    public ResponseEntity<CartResponseDto> removeCartItem(@PathVariable UUID menuItemId){
        return ResponseEntity.ok(
                cartService.removeCartItem(menuItemId)
        );
    }

    @PutMapping()
    public ResponseEntity<Void> clearCart(){
        cartService.clearLoggedInCustomerCart();
        return ResponseEntity.noContent().build();
    }
}
