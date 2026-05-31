package com.mentorship.food_delivery_app.restaurant.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record RestaurantRatePatchRequestDto(

        @Size(max = 100, message = "Title must not exceed 100 characters.")
        String title,

        @DecimalMin(value = "0.0", message = "Rating must be at least 0.0.")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5.0.")
        Double rating,

        @Size(max = 500, message = "Comment must not exceed 500 characters.")
        String comment

) {}
