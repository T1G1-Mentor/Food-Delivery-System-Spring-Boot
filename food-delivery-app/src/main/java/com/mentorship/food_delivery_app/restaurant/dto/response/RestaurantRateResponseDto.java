package com.mentorship.food_delivery_app.restaurant.dto.response;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantRate;

import java.time.Instant;
import java.util.UUID;

public record RestaurantRateResponseDto(

        UUID id,
        String title,
        Double rating,
        String comment,
        String customerFullName,
        Instant createdAt

) {
    public static RestaurantRateResponseDto from(RestaurantRate rate) {
        return new RestaurantRateResponseDto(
                rate.getId(),
                rate.getTitle(),
                rate.getRating(),
                rate.getComment(),
                rate.getCustomer().getFullName(),
                rate.getCreatedAt()
        );
    }
}
