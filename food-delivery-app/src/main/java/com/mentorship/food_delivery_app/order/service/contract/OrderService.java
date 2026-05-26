package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderRequestDto request);

    void updateOrderStatus(UUID orderId);

    void cancelOrder(UUID orderId);

    Page<OrderListItemDto> listOrders(UUID restaurantBranchId, OrderStatus status, Pageable pageable);

    OrderDetailsDto getOrderDetails(UUID orderId);

    Page<OrderListItemDto> getCustomerOrderHistory(OrderStatus status, Pageable pageable);
}
