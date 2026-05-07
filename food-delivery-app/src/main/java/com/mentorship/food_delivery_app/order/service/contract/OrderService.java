package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.request.UpdateOrderStatusRequestDto;

public interface OrderService {

    void updateOrderStatus(UpdateOrderStatusRequestDto request);
}
