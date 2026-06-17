package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.ChangeMenuStatusDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/restaurants/branchs/{branchId}")
@RequiredArgsConstructor
public class RestaurantManagementController {
    private final RestaurantService restaurantService;


    // -------------------------------------------------------------------
    //  MENU MANAGEMENT
    // -------------------------------------------------------------------

    @PostMapping("/restaurant-menus")
    public ResponseEntity<Void> createRestaurantMenu(@RequestBody @Valid CreateMenuDto createMenuDto,
                                                     @PathVariable UUID branchId) {
        restaurantService.createRestaurantMenu(createMenuDto, branchId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/restaurant-menus/{menuId}")
    public ResponseEntity<Void> updateRestaurantMenu(@RequestBody @Valid UpdateMenuDto updateMenuDto,
                                                     @PathVariable UUID menuId,
                                                     @PathVariable UUID branchId) {
        restaurantService.updateRestaurantMenu(updateMenuDto
                , menuId
                , branchId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/restaurant-menus/{menuId}")
    public ResponseEntity<Void> deleteRestaurantMenu(@PathVariable UUID menuId,
                                                     @PathVariable UUID branchId) {
        restaurantService.deleteRestaurantMenu(menuId, branchId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/restaurant-menus/{menuId}/status")
    public ResponseEntity<Void> toggleRestaurantMenuStatus(@PathVariable UUID menuId,
                                                           @PathVariable UUID branchId,
                                                           @RequestBody @Valid ChangeMenuStatusDto changeMenuStatusDto) {
        restaurantService.toggleRestaurantMenuStatus(menuId,
                branchId,
                changeMenuStatusDto.isEnabled());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // -------------------------------------------------------------------
    //  MENU ITEM MANAGEMENT
    // -------------------------------------------------------------------
    @PostMapping("/restaurant-menus/{restaurantMenuId}/menu-items")
    public ResponseEntity<Void> createMenuItem(@PathVariable UUID branchId,
                                               @PathVariable UUID restaurantMenuId,
                                               @RequestBody @Valid MenuItemRequestDto menuItemRequestDto) {

        restaurantService.createMenuItem(menuItemRequestDto,
                restaurantMenuId, branchId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/restaurant-menus/{restaurantMenuId}/menu-items")
    public ResponseEntity<Void> updateMenuItem(@PathVariable UUID branchId,
                                               @PathVariable UUID restaurantMenuId,
                                               @RequestBody @Valid UpdateMenuItemRequestDto menuItemRequestDto) {

        restaurantService.updateMenuItem(menuItemRequestDto,
                restaurantMenuId, branchId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/restaurant-menus/{restaurantMenuId}/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID branchId,
                                               @PathVariable UUID restaurantMenuId,
                                               @PathVariable UUID menuItemId) {

        restaurantService.deleteMenuItem(menuItemId,
                restaurantMenuId, branchId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
