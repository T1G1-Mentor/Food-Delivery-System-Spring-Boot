package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/restaurants/branchs/{branchId}")
@RequiredArgsConstructor
public class RestaurantMenuManagementController {
    private final RestaurantMenuService restaurantMenuService;



    // -------------------------------------------------------------------
    //  MENU ITEM MANAGEMENT
    // -------------------------------------------------------------------
    @PostMapping("/restaurant-menus/{restaurantMenuId}/menu-items")
    public ResponseEntity<Void> createMenuItem(@PathVariable UUID branchId,
                                               @PathVariable UUID restaurantMenuId,
                                               @RequestBody @Valid MenuItemRequestDto menuItemRequestDto){

        restaurantMenuService.createMenuItem(menuItemRequestDto,
                restaurantMenuId, branchId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
