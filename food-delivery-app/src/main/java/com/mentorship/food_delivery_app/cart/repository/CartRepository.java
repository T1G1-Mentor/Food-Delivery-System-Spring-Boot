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
    @Query("""
    SELECT c FROM Cart c
    WHERE c.customer.customerId = :customerId
""")
    Optional<Cart> findByCustomerId(UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cart c WHERE c.cartId = :id")
    Optional<Cart> findByIdWithLock(@Param("id") UUID id);

    @Query("""
                SELECT c FROM Cart c
                LEFT JOIN FETCH c.cartItems ci
                JOIN FETCH ci.menuItem mi
                WHERE c.customer.customerId= :customerId
            """)
    Optional<Cart> findWithCartItemsAndMenuItemsByCustomerId(UUID customerId);

    @Query("""
    SELECT c FROM Cart c
    JOIN FETCH c.currentRestaurant
    WHERE c.cartId = :cartId AND c.customer.customerId = :customerId
""")
    Optional<Cart> findCarWithRestaurantBranchByIdAndCustomerId(UUID cartId, UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT c FROM Cart c
    JOIN FETCH c.currentRestaurant
    WHERE c.cartId = :cartId AND c.customer.customerId = :customerId
""")
    Cart findAndLockWithRestBranchByIdAndCustomerId(UUID cartId, UUID customerId);
}
