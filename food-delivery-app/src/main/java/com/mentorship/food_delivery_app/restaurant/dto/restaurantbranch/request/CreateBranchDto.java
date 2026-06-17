package com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalTime;

public record CreateBranchDto(
        @PositiveOrZero
        BigDecimal deliveryFee,

        @PositiveOrZero
        BigDecimal minOrder,

        @NotBlank
        @Size(max = 20)
        String city,

        @NotNull
        LocalTime openTime,

        @NotNull
        LocalTime closeTime,

        @NotBlank
        @Size(max = 15)
        String phoneNumber,

        @Positive
        Integer estimatedDeliveryTime
) {}
