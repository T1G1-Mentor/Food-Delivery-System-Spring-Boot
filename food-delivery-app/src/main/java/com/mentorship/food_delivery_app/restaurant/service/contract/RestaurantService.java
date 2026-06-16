package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import org.springframework.transaction.annotation.Transactional;

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

    void createMenuItem(MenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID branchId);

    void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto
            , UUID restaurantMenuId, UUID branchId);

    void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId, UUID branchId);

    List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId, UUID branchId);

    void createRestaurantMenu(CreateMenuDto createMenuDto, UUID branchId);

    @Transactional
    void updateRestaurantMenu(UpdateMenuDto updateMenuDto, UUID menuId, UUID branchId);
}
