package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/restaurants/branches/{branchId}")
@RequiredArgsConstructor
public class PublicRestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/restaurant-menus/{restaurantMenuId}/menu-items")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItemsByMenuId(@PathVariable UUID restaurantMenuId,
                                                                     @PathVariable UUID branchId) {

        return ResponseEntity.ok(
                restaurantService
                        .getAllMenuItemsByMenuId(restaurantMenuId,
                                branchId)
        );
    }
}
