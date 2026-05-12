package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;

import java.util.UUID;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderRequestDto request);
    void updateOrderStatus(UUID orderId);
}
