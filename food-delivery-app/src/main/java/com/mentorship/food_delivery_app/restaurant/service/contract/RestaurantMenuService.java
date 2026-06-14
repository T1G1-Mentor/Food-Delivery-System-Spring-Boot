package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;

import java.util.UUID;

public interface RestaurantMenuService {
    void createMenuItem(MenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID restaurantId);

    RestaurantMenu getRestaurantMenuByIdAndBranchId(UUID restaurantMenuId, UUID branchId);
}
