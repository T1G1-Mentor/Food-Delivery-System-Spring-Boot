package com.mentorship.food_delivery_app.cart.repository;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    Optional<Cart> findByCustomerId(UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cart c WHERE c.id = :id")
    Optional<Cart> findByIdWithLock(@Param("id") UUID id);
}
