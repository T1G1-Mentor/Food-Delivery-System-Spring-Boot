package com.mentorship.food_delivery_app.cart.controller;

import com.mentorship.food_delivery_app.cart.dto.response.CartItemResponseDto;
import com.mentorship.food_delivery_app.cart.dto.response.CartResponseDto;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

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
