package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;

import java.util.List;

public interface DiscoveryService {
    List<SearchMenuItemResponse> searchMenuItem(String query);

    List<RestaurantDto> searchRestaurants(String name, String category);

    List<TopRestaurantDto> getTopRestaurants();
}
