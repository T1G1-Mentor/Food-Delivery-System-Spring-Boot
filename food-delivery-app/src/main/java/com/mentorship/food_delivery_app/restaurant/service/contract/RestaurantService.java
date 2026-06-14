package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    /**
     * Searches for menu item in the DB
     *
     * @param menuItemId UUID of the menu item in the DB
     * @return menuItem if it does exist
     *
     *
     */
    MenuItem getMenuItemById(UUID menuItemId);

    Coupon getRestaurantCoupon(UUID couponId);

    List<MenuItem> getMenuItemsByIds(List<UUID> menuItemIds);

    RestaurantBranch getRestaurantBranchById(UUID restaurantBranchId);

    void createMenuItem(MenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID restaurantId);
}
