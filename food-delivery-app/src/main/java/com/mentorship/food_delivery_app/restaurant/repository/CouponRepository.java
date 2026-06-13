package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends CrudRepository<Coupon, UUID> {
    @Query("""
    SELECT c FROM Coupon c
    WHERE c.couponId = :couponId
""")
    Optional<Coupon> findByCouponId(UUID couponId);
}
