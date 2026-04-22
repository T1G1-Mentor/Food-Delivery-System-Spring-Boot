package com.mentorship.food_delivery_app.cart.repository;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCart(@NotNull(message = "Item id must be provided.") UUID cartItemId, Cart userCart);
}
