package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            WHERE (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:categoryName IS NULL OR EXISTS (
                SELECT 1 FROM r.categories c
                WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))
            ))
            """)
    List<Restaurant> searchRestaurants(String name, String categoryName);
}

