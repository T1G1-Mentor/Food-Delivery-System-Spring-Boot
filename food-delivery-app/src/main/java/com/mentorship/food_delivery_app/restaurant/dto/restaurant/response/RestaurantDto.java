package com.mentorship.food_delivery_app.restaurant.dto.restaurant.response;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantRate;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;

import java.util.OptionalDouble;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record RestaurantDto(
        UUID restaurantId,
        String name,
        String description,
        Set<String> categories,
        Double averageRating,
        Long ratingCount
) {
    public static RestaurantDto from(Restaurant restaurant) {
        Set<String> categoryNames = restaurant.getCategories() != null
                ? restaurant.getCategories().stream()
                        .map(c -> c.getName())
                        .collect(Collectors.toSet())
                : Set.of();

        Set<RestaurantRate> ratings = restaurant.getRatings();
        long ratingCount = 0L;
        double averageRating = 0.0;

        if (ratings != null && !ratings.isEmpty()) {
            ratingCount = ratings.size();
            OptionalDouble avg = ratings.stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(RestaurantRate::getRating)
                    .average();
            averageRating = avg.orElse(0.0);
        }

        return new RestaurantDto(
                restaurant.getRestaurantId(),
                restaurant.getName(),
                restaurant.getDescription(),
                categoryNames,
                averageRating,
                ratingCount
        );
    }
}
