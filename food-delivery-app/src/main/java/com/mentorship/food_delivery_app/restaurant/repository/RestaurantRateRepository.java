package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRateRepository extends JpaRepository<RestaurantRate, UUID> {
}
