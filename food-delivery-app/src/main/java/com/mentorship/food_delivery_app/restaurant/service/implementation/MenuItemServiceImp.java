package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImp implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    @Transactional
    @Override
    public MenuItem createMenuItem(MenuItemRequestDto menuItemRequestDto,
                               RestaurantMenu restaurantMenu) {
        MenuItem menuItem = buildMenuItem(menuItemRequestDto);
        menuItem.setMenu(restaurantMenu);
        return menuItemRepository.save(menuItem);
    }

    private MenuItem buildMenuItem(MenuItemRequestDto menuItemRequestDto){
        return MenuItem.builder()
                .description(menuItemRequestDto.menuItemDescription())
                .name(menuItemRequestDto.menuItemName())
                .price(menuItemRequestDto.menuItemPrice())
                .isAvailable(true)
                .build();
    }
}

