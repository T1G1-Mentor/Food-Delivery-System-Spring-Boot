package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import org.springframework.stereotype.Component;

@Component
public class OrderTrackingMapper {
    public OrderTrackingDto toResponse(OrderTracking tracking) {
        return new OrderTrackingDto(
                tracking.getStatus(),
                tracking.getDescription(),
                tracking.getCreatedAt());
    }
}
