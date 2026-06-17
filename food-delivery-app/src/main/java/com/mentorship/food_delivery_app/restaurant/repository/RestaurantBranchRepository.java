package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantBranchRepository extends CrudRepository<RestaurantBranch, UUID> {

    @Query("""
            SELECT r FROM RestaurantBranch r
            WHERE r.restaurantBranchId = :restaurantBranchId
            """)
    Optional<RestaurantBranch> findByRestaurantBranchId(UUID restaurantBranchId);

    @Query("""
            SELECT b.isEnabled FROM RestaurantBranch b
            WHERE b.restaurantBranchId = :branchId
            """)
    Boolean isEnabledById(UUID branchId);

    @Query("""
            SELECT b FROM RestaurantBranch b
            WHERE b.restaurant.restaurantId = :restaurantId
            """)
    List<RestaurantBranch> findAllByRestaurantId(UUID restaurantId);

    @Query("""
            SELECT b FROM RestaurantBranch b
            WHERE b.restaurantBranchId = :branchId
            AND b.restaurant.restaurantId = :restaurantId
            """)
    Optional<RestaurantBranch> findByBranchIdAndRestaurantId(UUID branchId, UUID restaurantId);
}

