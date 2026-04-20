package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestaurantBranchRepository extends CrudRepository<RestaurantBranch, UUID> {
}
