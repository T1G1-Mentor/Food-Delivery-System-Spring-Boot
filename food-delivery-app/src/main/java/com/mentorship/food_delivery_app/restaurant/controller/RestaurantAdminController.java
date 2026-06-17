package com.mentorship.food_delivery_app.restaurant.controller;

import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.CreateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.UpdateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.CreateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.UpdateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.response.RestaurantBranchDto;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/restaurants")
@RequiredArgsConstructor
public class RestaurantAdminController {

    private final RestaurantAdminService restaurantAdminService;

    // -------------------------------------------------------------------
    //  RESTAURANT MANAGEMENT
    // -------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@RequestBody @Valid CreateRestaurantDto dto) {
        restaurantAdminService.createRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable UUID restaurantId,
                                                  @RequestBody @Valid UpdateRestaurantDto dto) {
        restaurantAdminService.updateRestaurant(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID restaurantId) {
        restaurantAdminService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(restaurantAdminService.searchRestaurants(name, category));
    }

    // -------------------------------------------------------------------
    //  BRANCH MANAGEMENT
    // -------------------------------------------------------------------

    @PostMapping("/{restaurantId}/branches")
    public ResponseEntity<Void> createBranch(@PathVariable UUID restaurantId,
                                              @RequestBody @Valid CreateBranchDto dto) {
        restaurantAdminService.createBranch(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{restaurantId}/branches/{branchId}")
    public ResponseEntity<Void> updateBranch(@PathVariable UUID restaurantId,
                                              @PathVariable UUID branchId,
                                              @RequestBody @Valid UpdateBranchDto dto) {
        restaurantAdminService.updateBranch(restaurantId, branchId, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{restaurantId}/branches/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID restaurantId,
                                              @PathVariable UUID branchId) {
        restaurantAdminService.deleteBranch(restaurantId, branchId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{restaurantId}/branches")
    public ResponseEntity<List<RestaurantBranchDto>> getBranches(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(restaurantAdminService.getBranchesByRestaurantId(restaurantId));
    }
}
