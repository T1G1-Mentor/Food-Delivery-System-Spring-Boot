package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.DiscoveryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/discover")
@RequiredArgsConstructor
@Tag(name = "Discovery Controller", description = "More like the home pag to browse restaurants, menus, and menu items")
public class PublicDiscoveryController {
    private final DiscoveryService discoveryService;

    @GetMapping("/menu-items")
    public ResponseEntity<List<SearchMenuItemResponse>> searchMenuIte(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(
                discoveryService.searchMenuItem(query
                )
        );
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDto>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(discoveryService.searchRestaurants(name, category));
    }

    @GetMapping("/restaurants/top")
    public ResponseEntity<List<TopRestaurantDto>> getTopRestaurants() {
        return ResponseEntity.ok(discoveryService.getTopRestaurants());
    }
}
