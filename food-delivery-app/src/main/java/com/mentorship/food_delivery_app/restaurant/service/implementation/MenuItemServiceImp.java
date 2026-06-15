package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import com.mentorship.food_delivery_app.restaurant.exceptions.MenuItemNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImp implements MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Transactional
    @Override
    public void createMenuItem(MenuItemRequestDto menuItemRequestDto,
                               RestaurantMenu restaurantMenu) {
        MenuItem menuItem = MenuItem.buildMenuItem(menuItemRequestDto.menuItemName(),
                menuItemRequestDto.menuItemDescription(),
                menuItemRequestDto.menuItemPrice());

        menuItem.setMenu(restaurantMenu);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    @Override
    public void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId){
//        to validate that the menu item belongs to that menu
        MenuItem menuItem = getMenuItemByIdAndMenuId(menuItemId,
                restaurantMenuId);
        menuItemRepository.deleteById(menuItem.getMenuItemId());
    }
    @Transactional
    @Override
    public void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId) {

        MenuItem menuItem = getMenuItemByIdAndMenuId(menuItemRequestDto.menuItemId()
                , restaurantMenuId);

        menuItem.applyModifications(menuItemRequestDto.menuItemName(),
                menuItemRequestDto.menuItemDescription(),
                menuItemRequestDto.menuItemPrice());

    }

    @Override
    public MenuItem getMenuItemByIdAndMenuId(UUID menuItemId, UUID restaurantMenuId) {
        return menuItemRepository.findByIdAndMenuId(menuItemId, restaurantMenuId)
                .orElseThrow(() -> new MenuItemNotFoundException(ErrorMessage.MENU_ITEM_NOT_FOUND.getMessage()));
    }

}

