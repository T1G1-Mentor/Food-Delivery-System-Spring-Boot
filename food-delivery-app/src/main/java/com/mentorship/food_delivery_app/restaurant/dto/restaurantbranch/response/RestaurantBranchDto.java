package com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.response;

import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record RestaurantBranchDto(
        UUID branchId,
        String city,
        BigDecimal deliveryFee,
        BigDecimal minOrder,
        LocalTime openTime,
        LocalTime closeTime,
        String phoneNumber,
        Integer estimatedDeliveryTime,
        boolean isEnabled
) {
    public static RestaurantBranchDto from(RestaurantBranch branch) {
        return new RestaurantBranchDto(
                branch.getRestaurantBranchId(),
                branch.getCity(),
                branch.getDeliveryFee(),
                branch.getMinOrder(),
                branch.getOpenTime(),
                branch.getCloseTime(),
                branch.getPhoneNumber(),
                branch.getEstimatedDeliveryTime(),
                branch.isEnabled()
        );
    }
}
