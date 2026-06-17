package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            SELECT new com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto(
                r.restaurantId, r.name, r.description, COALESCE(AVG(rr.rating), 0.0), COUNT(rr)
            )
            FROM Restaurant r
            LEFT JOIN r.ratings rr
            GROUP BY r.restaurantId, r.name, r.description
            ORDER BY COALESCE(AVG(rr.rating), 0.0) DESC, COUNT(rr) DESC
            """)
    List<TopRestaurantDto> findTopByAverageRating(Pageable pageable);
}

