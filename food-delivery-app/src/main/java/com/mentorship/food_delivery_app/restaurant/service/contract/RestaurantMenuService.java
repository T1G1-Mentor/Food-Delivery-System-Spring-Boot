package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.response.RestaurantMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;

import java.util.List;
import java.util.UUID;

public interface RestaurantMenuService {
    void createMenuItem(MenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID branchId);

    RestaurantMenu getRestaurantMenuByIdAndBranchId(UUID restaurantMenuId, UUID branchId);

    void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto
            , UUID restaurantMenuId, UUID branchId);

    void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId, UUID branchId);

    List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId, UUID branchId);

    void createRestaurantMenu(CreateMenuDto createMenuDto, RestaurantBranch branch);

    void updateRestaurantMenu(UpdateMenuDto updateMenuDto, UUID menuId, UUID branchId);

    void deleteRestaurantMenu(UUID menuId, UUID branchId);

    void toggleRestaurantMenuStatus(UUID menuId, UUID branchId, Boolean isEnabled);

    List<RestaurantMenuDto> getAllMenusByBranchId(UUID branchId);
}
