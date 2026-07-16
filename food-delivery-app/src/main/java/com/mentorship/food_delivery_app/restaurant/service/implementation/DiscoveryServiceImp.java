package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.restaurant.dto.Pagination;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscoveryServiceImp implements DiscoveryService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;


    @Transactional(readOnly = true)
    @Override
    public Pagination searchMenuItem(String query, int pageSize, UUID nextCursor) {
        pageSize++;
        List<SearchMenuItemResponse> responses = menuItemRepository.searchMenuItem(query,
                pageSize,
                nextCursor);
        Pagination pagination;

        if (responses.size() == pageSize) {
            responses.removeLast();

            pagination = new Pagination(true,
                    responses.getLast().getMenuItemId(),
                    responses);
        } else pagination = new Pagination(false,
                null,
                responses);

        return pagination;
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
        return restaurantRepository.findTopNByAverageRating(10);
    }
}
