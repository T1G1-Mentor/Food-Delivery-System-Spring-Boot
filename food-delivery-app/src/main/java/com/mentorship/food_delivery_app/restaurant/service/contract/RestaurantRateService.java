package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRatePatchRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRateRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.response.RestaurantRateResponseDto;

import java.util.List;
import java.util.UUID;

public interface RestaurantRateService {

    List<RestaurantRateResponseDto> getRestaurantRatings(UUID restaurantId);

    RestaurantRateResponseDto createRating(UUID restaurantId, RestaurantRateRequestDto request);

    RestaurantRateResponseDto updateRating(UUID restaurantId, UUID rateId, RestaurantRatePatchRequestDto request);

    void deleteRating(UUID restaurantId, UUID rateId);
}
