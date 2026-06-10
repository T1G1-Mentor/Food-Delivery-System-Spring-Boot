package com.mentorship.food_delivery_app.order.dto.response;

import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponseDto(
        UUID orderId,
        UUID restaurantBranchId,
        String restaurantName,
        BigDecimal orderSubtotal,
        BigDecimal orderFee,
        BigDecimal orderDiscount,
        BigDecimal orderTotal,
        DeliveryAddressDto orderDeliveryAddress,
        List<OrderItemResponseDto> orderItems
) {

}
