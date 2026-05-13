package com.mentorship.food_delivery_app.order.service.contract;

import java.util.UUID;

public interface OrderService {

    void updateOrderStatus(UUID orderId);

    void cancelOrder(UUID orderId);
}
