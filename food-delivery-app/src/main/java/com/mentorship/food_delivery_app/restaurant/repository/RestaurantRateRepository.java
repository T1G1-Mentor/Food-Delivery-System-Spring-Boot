package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRateRepository extends JpaRepository<RestaurantRate, UUID> {

    @Query("""
            SELECT r FROM RestaurantRate r
            JOIN FETCH r.customer c
            JOIN FETCH c.user
            WHERE r.restaurant.id = :restaurantId
            """)
    List<RestaurantRate> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId);

    @Query("""
            SELECT r FROM RestaurantRate r
            JOIN FETCH r.customer c
            JOIN FETCH c.user
            WHERE r.id = :rateId AND r.customer.id = :customerId
            """)
    Optional<RestaurantRate> findByIdAndCustomerId(@Param("rateId") UUID rateId,
                                                   @Param("customerId") UUID customerId);

    long countByRestaurantIdAndCustomerId(UUID restaurantId, UUID customerId);
}
