package com.mentorship.food_delivery_app.restaurant.dto.restaurant.response;

import java.util.UUID;

public interface TopRestaurantDto {
    UUID getRestaurantId();

    String getRestaurantName();

    String getRestaurantDescription();

    Double getAverageRating();

    Long getRatingCount();
}
