package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
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
        RestaurantMenu restaurantMenu = this.getRestaurantMenuByIdAndBranchId(restaurantMenuId, branchId);

        menuItemService.createMenuItem(menuItemRequestDto, restaurantMenu);
    }

    @Transactional
    public void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto,
                               UUID restaurantMenuId,
                               UUID branchId) {
        // we first validate that this menu belongs to the restaurant
        validateRestaurantMenuExists(restaurantMenuId, branchId);

        menuItemService.updateMenuItem(menuItemRequestDto,
                restaurantMenuId);
    }

    @Transactional
    @Override
    public void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId, UUID branchId) {
        validateRestaurantMenuExists(restaurantMenuId, branchId);

        menuItemService.deleteMenuItem(menuItemId,
                restaurantMenuId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId, UUID branchId) {
        validateRestaurantMenu(restaurantMenuId, branchId);

        return menuItemService
                .getAllMenuItemsByMenuId(restaurantMenuId);
    }


    @Override
    public RestaurantMenu getRestaurantMenuByIdAndBranchId(UUID restaurantMenuId, UUID branchId) {
        return restaurantMenuRepository.findByIdAndBranchId(restaurantMenuId, branchId)
                .orElseThrow(() -> new RestaurantMenuNotFoundException
                        (ErrorMessage.RESTAURANT_MENU_NOT_FOUND.getMessage()));
    }

    @Transactional
    @Override
    public void createRestaurantMenu(CreateMenuDto createMenuDto, RestaurantBranch branch) {

        RestaurantMenu menu = RestaurantMenu.
                createMenu(createMenuDto.restaurantMenuName());
        menu.setRestaurantBranch(branch);

        restaurantMenuRepository.save(menu);
    }

    @Transactional
    @Override
    public void updateRestaurantMenu(UpdateMenuDto updateMenuDto, UUID menuId, UUID branchId) {
        RestaurantMenu menu = getRestaurantMenuByIdAndBranchId(menuId,
                branchId);

        menu.applyModifications(updateMenuDto.restaurantMenuName());
    }

    private boolean validateRestaurantMenuExists(UUID menuId, UUID branchId) {
        Boolean isEnabled = restaurantMenuRepository.isEnabledByIdAndBranchId(menuId, branchId);

        if (isEnabled == null)
            throw new RestaurantMenuNotFoundException(ErrorMessage.RESTAURANT_MENU_NOT_FOUND.getMessage());

        return isEnabled;
    }

    private void validateRestaurantMenu(UUID menuId, UUID branchId) {
        if (!validateRestaurantMenuExists(menuId, branchId))
            throw new DisabledRestaurantMenuException(ErrorMessage.RESTAURANT_MENU_DISABLED.getMessage());
    }
}
