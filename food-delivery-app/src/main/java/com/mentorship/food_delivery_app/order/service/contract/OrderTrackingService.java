package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;

import java.util.List;
import java.util.UUID;

public interface OrderTrackingService {
    List<OrderTrackingDto> getTrackingHistory(UUID customerId, UUID orderId);

    List<OrderTrackingDto> getTrackingHistoryByOrderId(UUID orderId);
}
