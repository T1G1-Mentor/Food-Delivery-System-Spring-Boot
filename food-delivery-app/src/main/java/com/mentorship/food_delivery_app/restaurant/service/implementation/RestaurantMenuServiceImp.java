package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import com.mentorship.food_delivery_app.restaurant.exceptions.DisabledRestaurantMenuException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantMenuNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantMenuRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.MenuItemService;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantMenuServiceImp implements RestaurantMenuService {
    private final RestaurantMenuRepository restaurantMenuRepository;
    private final MenuItemService menuItemService;

    @Transactional
    @Override
    public void createMenuItem(MenuItemRequestDto menuItemRequestDto
            , UUID restaurantMenuId
            , UUID branchId) {
        RestaurantMenu restaurantMenu = this.getRestaurantMenuByIdAndBranchId(restaurantMenuId, branchId);

        if (!restaurantMenu.isEnabled())
            throw new DisabledRestaurantMenuException(ErrorMessage.RESTAURANT_MENU_DISABLED.getMessage());

         menuItemService.createMenuItem(menuItemRequestDto,restaurantMenu);
    }

    @Override
    public RestaurantMenu getRestaurantMenuByIdAndBranchId(UUID restaurantMenuId, UUID branchId) {
        return restaurantMenuRepository.findByIdAndBranchId(restaurantMenuId, branchId)
                .orElseThrow(()-> new RestaurantMenuNotFoundException
                        (ErrorMessage.RESTAURANT_MENU_NOT_FOUND.getMessage()));
    }
}
