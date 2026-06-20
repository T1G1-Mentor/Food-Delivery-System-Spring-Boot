package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRatePatchRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRateRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.response.RestaurantRateResponseDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer/restaurant/{restaurantId}/rate")
@RequiredArgsConstructor
@Tag(name = "Restaurant Rate Controller", description = "For customers to add ratings and comments for restaurants")
public class RestaurantRateController {

    private final RestaurantRateService restaurantRateService;

    @GetMapping
    public ResponseEntity<List<RestaurantRateResponseDto>> getRestaurantRatings(
            @PathVariable UUID restaurantId) {
        return ResponseEntity.ok(restaurantRateService.getRestaurantRatings(restaurantId));
    }

    @PostMapping
    public ResponseEntity<RestaurantRateResponseDto> createRating(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody RestaurantRateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantRateService.createRating(restaurantId, request));
    }

    @PatchMapping("/{rateId}")
    public ResponseEntity<RestaurantRateResponseDto> updateRating(
            @PathVariable UUID restaurantId,
            @PathVariable UUID rateId,
            @Valid @RequestBody RestaurantRatePatchRequestDto request) {
        return ResponseEntity.ok(restaurantRateService.updateRating(restaurantId, rateId, request));
    }

    @DeleteMapping("/{rateId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable UUID restaurantId,
            @PathVariable UUID rateId) {
        restaurantRateService.deleteRating(restaurantId, rateId);
        return ResponseEntity.noContent().build();
    }
}
