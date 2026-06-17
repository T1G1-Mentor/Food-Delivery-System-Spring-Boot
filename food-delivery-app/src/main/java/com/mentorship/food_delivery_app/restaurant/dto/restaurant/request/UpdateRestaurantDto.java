package com.mentorship.food_delivery_app.restaurant.dto.restaurant.request;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateRestaurantDto(
        @Size(min = 2, max = 100)
        String name,

        @Size(min = 5, max = 255)
        String description,

        Set<Integer> categoryIds
) {}
