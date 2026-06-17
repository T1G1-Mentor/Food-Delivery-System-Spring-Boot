package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;

import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    void createMenuItem(MenuItemRequestDto menuItemRequestDto, RestaurantMenu restaurantMenu);

    void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId);

    MenuItem getMenuItemByIdAndMenuId(UUID menuItemId, UUID restaurantMenuId);

    void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId);

    List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId);
}
