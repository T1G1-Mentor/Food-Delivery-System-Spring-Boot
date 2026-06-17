package com.mentorship.food_delivery_app.restaurant.dto.restaurant.response;

import java.util.UUID;

public record TopRestaurantDto(
        UUID restaurantId,
        String name,
        String description,
        Double averageRating,
        Long ratingCount
) {}
