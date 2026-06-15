package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import com.mentorship.food_delivery_app.restaurant.exceptions.DisabledRestaurantMenuException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantMenuNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantMenuRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.MenuItemService;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        RestaurantMenu restaurantMenu = getAndValidateRestaurantMenu(restaurantMenuId, branchId);

         menuItemService.createMenuItem(menuItemRequestDto,restaurantMenu);
    }

    @Transactional
    public void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto,
                               UUID restaurantMenuId,
                               UUID branchId){
        // we first validate that this menu belongs to the restaurant
        RestaurantMenu restaurantMenu = getAndValidateRestaurantMenu(restaurantMenuId, branchId);

        menuItemService.updateMenuItem(menuItemRequestDto,
                restaurantMenu.getRestaurantMenuId());
    }

    @Transactional
    @Override
    public void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId, UUID branchId) {
        RestaurantMenu restaurantMenu= getAndValidateRestaurantMenu(restaurantMenuId,
                branchId);
        menuItemService.deleteMenuItem(menuItemId,
                restaurantMenu.getRestaurantMenuId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId, UUID branchId) {
        RestaurantMenu restaurantMenu = getRestaurantMenuByIdAndBranchId(restaurantMenuId,
                branchId);
        if (!restaurantMenu.isEnabled())
            throw new DisabledRestaurantMenuException(ErrorMessage.RESTAURANT_MENU_DISABLED.getMessage());


        return menuItemService
                .getAllMenuItemsByMenuId(restaurantMenuId);
    }

    private RestaurantMenu getAndValidateRestaurantMenu(UUID restaurantMenuId, UUID branchId) {
        return this.getRestaurantMenuByIdAndBranchId(restaurantMenuId, branchId);

//        if (!restaurantMenu.isEnabled())
//            throw new DisabledRestaurantMenuException(ErrorMessage.RESTAURANT_MENU_DISABLED.getMessage());

//        return restaurantMenu;
    }

    @Override
    public RestaurantMenu getRestaurantMenuByIdAndBranchId(UUID restaurantMenuId, UUID branchId) {
        return restaurantMenuRepository.findByIdAndBranchId(restaurantMenuId, branchId)
                .orElseThrow(()-> new RestaurantMenuNotFoundException
                        (ErrorMessage.RESTAURANT_MENU_NOT_FOUND.getMessage()));
    }
}
