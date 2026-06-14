package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;

public interface MenuItemService {
    void createMenuItem(MenuItemRequestDto menuItemRequestDto, RestaurantMenu restaurantMenu);
}
