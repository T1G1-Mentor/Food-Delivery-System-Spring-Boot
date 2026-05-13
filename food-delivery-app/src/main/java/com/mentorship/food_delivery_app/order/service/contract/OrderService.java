package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderSummaryDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    void updateOrderStatus(UUID orderId);

    Page<OrderListItemDto> listOrders(OrderStatus status, Pageable pageable);

    OrderDetailsDto getOrderDetails(UUID orderId);

    OrderSummaryDto getOrderSummary(UUID orderId);

    Page<OrderListItemDto> getCustomerOrderHistory(OrderStatus status, Pageable pageable);
}
