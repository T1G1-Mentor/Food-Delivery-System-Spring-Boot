package com.mentorship.food_delivery_app.restaurant.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record RestaurantRatePatchRequestDto(

        @Size(max = 100, message = "Title must not exceed 100 characters.")
        String title,

        @Min(value = 1, message = "Rating must be at least 1.")
        @Max(value = 5, message = "Rating must be at most 5.")
        Integer rating,

        @Size(max = 500, message = "Comment must not exceed 500 characters.")
        String comment

) {}
