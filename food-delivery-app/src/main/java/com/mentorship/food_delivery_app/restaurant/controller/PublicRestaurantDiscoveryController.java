package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/restaurants")
@RequiredArgsConstructor
public class PublicRestaurantDiscoveryController {

    private final RestaurantAdminService restaurantAdminService;

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(restaurantAdminService.searchRestaurants(name, category));
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopRestaurantDto>> getTopRestaurants() {
        return ResponseEntity.ok(restaurantAdminService.getTopRestaurants());
    }
}
