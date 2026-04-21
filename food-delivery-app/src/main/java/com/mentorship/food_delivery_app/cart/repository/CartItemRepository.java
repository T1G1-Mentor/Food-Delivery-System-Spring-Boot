package com.mentorship.food_delivery_app.cart.repository;

import com.mentorship.food_delivery_app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    @Modifying
    @Query("""
            DELETE FROM CartItem ci WHERE ci.cart.id = :cartId
            """)
    void deleteCartItemsByCartId(UUID cartId);
}
