package com.mentorship.food_delivery_app.restaurant.dto.restaurant.response;

import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record RestaurantDto(
        UUID restaurantId,
        String name,
        String description,
        Set<String> categories
) {
    public static RestaurantDto from(Restaurant restaurant) {
        Set<String> categoryNames = restaurant.getCategories() != null
                ? restaurant.getCategories().stream()
                        .map(c -> c.getName())
                        .collect(Collectors.toSet())
                : Set.of();

        return new RestaurantDto(
                restaurant.getRestaurantId(),
                restaurant.getName(),
                restaurant.getDescription(),
                categoryNames
        );
    }
}
