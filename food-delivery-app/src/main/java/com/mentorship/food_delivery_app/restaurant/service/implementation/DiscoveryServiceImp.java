package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryServiceImp implements DiscoveryService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;


    @Transactional(readOnly = true)
    @Override
    public List<SearchMenuItemResponse> searchMenuItem(String query) {
        return menuItemRepository.searchMenuItem(query);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RestaurantDto> searchRestaurants(String name, String categoryName) {
        return restaurantRepository.searchRestaurants(name, categoryName)
                .stream()
                .map(RestaurantDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TopRestaurantDto> getTopRestaurants() {
        return restaurantRepository.findTopByAverageRating(PageRequest.of(0, 10));
    }
}
