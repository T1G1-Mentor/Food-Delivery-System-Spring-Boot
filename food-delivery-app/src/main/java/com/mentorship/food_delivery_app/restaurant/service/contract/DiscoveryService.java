package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.Pagination;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;

import java.util.List;
import java.util.UUID;

public interface DiscoveryService {
    Pagination searchMenuItem(String query, int pageSize, UUID nextCursor);

    List<RestaurantDto> searchRestaurants(String name, String category);

    List<TopRestaurantDto> getTopRestaurants();
}
