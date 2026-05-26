package com.mentorship.food_delivery_app.order.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        BigDecimal subTotal,
        BigDecimal fee,
        BigDecimal discount,
        BigDecimal total,
        List<OrderItemResponseDto> orderItems
) {

}
