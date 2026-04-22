package com.mentorship.food_delivery_app.cart.repository;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    Optional<Cart> findByCustomerId(UUID id);
}
