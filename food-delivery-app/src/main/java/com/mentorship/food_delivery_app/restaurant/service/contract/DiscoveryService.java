package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;

import java.util.List;

public interface DiscoveryService {
    List<SearchMenuItemResponse> searchMenuItem(String query);

}
