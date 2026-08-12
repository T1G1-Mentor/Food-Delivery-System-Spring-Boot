package com.mentorship.food_delivery_app.order.service.contract;

import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderRequestDto request, UUID customerId);

    void handlerOrderStatusUpdate(UUID orderId, UUID userId);

    void cancelOrder(UUID orderId, UUID userId);

    Page<OrderListItemDto> listOrders(UUID restaurantBranchId, OrderStatus status, Pageable pageable);

     OrderDetailsDto getOrderDetails(UUID orderId, CustomerPrincipal customerPrincipal);

    Page<OrderListItemDto> getCustomerOrderHistory(OrderStatus status, Pageable pageable, CustomerPrincipal customerPrincipal);

    List<OrderTrackingDto> getOrderTrackingHistory(UUID customerId, UUID orderId);
}
