package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, UUID> {

    @Query("""
    SELECT rm FROM RestaurantMenu rm
    WHERE rm.restaurantMenuId = :restaurantMenuId AND rm.restaurantBranch.restaurantBranchId = :branchId
""")
    Optional<RestaurantMenu> findByIdAndBranchId(UUID restaurantMenuId, UUID branchId);

    @Query(""" 
    SELECT m.isEnabled FROM RestaurantMenu m
    WHERE m.restaurantMenuId = :menuId AND m.restaurantBranch.restaurantBranchId = :branchId
""")
    Boolean isEnabledByIdAndBranchId(UUID menuId, UUID branchId);
}
