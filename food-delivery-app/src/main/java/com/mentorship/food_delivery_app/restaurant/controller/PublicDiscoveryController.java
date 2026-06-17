package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.service.contract.DiscoveryService;
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
public class PublicDiscoveryController {
    private final DiscoveryService discoveryService;

    @GetMapping("/menu-items")
    public ResponseEntity<List<SearchMenuItemResponse>> searchMenuIte(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(
                discoveryService.searchMenuItem(query
                )
        );
    }
}
