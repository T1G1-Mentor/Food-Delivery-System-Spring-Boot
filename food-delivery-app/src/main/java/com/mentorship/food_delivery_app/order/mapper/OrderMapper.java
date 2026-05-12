package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderResponseDto toResponse(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getSubtotal(),
                order.getFee(),
                order.getDiscountValue(),
                order.getTotal(),
                order.getItems().stream().map(orderItemMapper::toResponse).toList()
        );
    }
}
