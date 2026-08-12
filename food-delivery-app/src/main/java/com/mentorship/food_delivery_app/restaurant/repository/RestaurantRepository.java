package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
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

    @Query(value = """
       SELECT r.restaurant_id          AS restaurantId,
               r.restaurant_name        AS restaurantName,
               r.restaurant_description AS restaurantDescription,
               rr.avg_rating            AS averageRating,
               rr.rating_count          AS ratingCount
        FROM (
            SELECT restaurant_rate_restaurant_id,
                   COALESCE(AVG(restaurant_rate_rating), 0.0)::DECIMAL(3,2) AS avg_rating,
                   COUNT(restaurant_rate_id) AS rating_count
            FROM restaurant_rate
            GROUP BY restaurant_rate_restaurant_id
            ORDER BY avg_rating DESC, rating_count DESC
            LIMIT :limit
        ) rr
        JOIN restaurant r ON rr.restaurant_rate_restaurant_id = r.restaurant_id
        ORDER BY rr.avg_rating DESC, rr.rating_count DESC
       """, nativeQuery = true)
    List<TopRestaurantDto> findTopNByAverageRating(int limit);
}

