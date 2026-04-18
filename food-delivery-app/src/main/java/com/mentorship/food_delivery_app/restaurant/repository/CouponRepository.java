package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CouponRepository extends CrudRepository<Coupon, UUID> {
}
