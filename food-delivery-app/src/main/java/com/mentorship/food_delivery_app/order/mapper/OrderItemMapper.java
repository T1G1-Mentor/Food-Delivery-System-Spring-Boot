package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.dto.response.OrderItemResponseDto;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemResponseDto toResponse(OrderItem orderItem) {
        return new OrderItemResponseDto(
                orderItem.getMenuItem().getId(),
                orderItem.getUnitPrice(),
                orderItem.getQuantity(),
                orderItem.getSubtotal()
        );
    }
}
