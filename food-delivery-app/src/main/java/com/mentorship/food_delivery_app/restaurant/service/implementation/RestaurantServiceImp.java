package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImp implements RestaurantService {
    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItem getMenuItemById(UUID menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ITEM_NOT_FOUND.getMessage()));
    }
}
