package com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalTime;

public record UpdateBranchDto(
        @PositiveOrZero
        BigDecimal deliveryFee,

        @PositiveOrZero
        BigDecimal minOrder,

        @Size(max = 20)
        String city,

        LocalTime openTime,

        LocalTime closeTime,

        @Size(max = 15)
        String phoneNumber,

        @Positive
        Integer estimatedDeliveryTime
) {}
