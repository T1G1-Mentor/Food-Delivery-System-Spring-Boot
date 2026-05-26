package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.mentorship.food_delivery_app.order.dto.response.*;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;

import java.util.Comparator;
import java.util.List;

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
                                order.getItems().stream().map(orderItemMapper::toResponse).toList());
        }

        public OrderListItemDto toListItem(Order order) {
                return new OrderListItemDto(
                                order.getOrderId(),
                                order.getStatus(),
                                order.getOrderDate(),
                                order.getTotal(),
                                order.getCustomerFullName(),
                                order.getRestaurantBranchName());
        }

        public OrderListItemDto toHistoryItem(Order order) {
                return new OrderListItemDto(
                                order.getOrderId(),
                                order.getStatus(),
                                order.getOrderDate(),
                                order.getTotal(),
                                order.getCustomerFullName(),
                                order.getRestaurantBranchName());
        }

        public OrderDetailsDto toDetails(Order order) {
                AddressDto address = new AddressDto(
                                order.getDeliveryCity(),
                                order.getDeliveryStreet(),
                                order.getDeliveryBuilding(),
                                order.getDeliveryApartment());

                List<OrderItemDto> items = order.getItems().stream()
                                .map(this::toOrderItem)
                                .toList();

                List<OrderTrackingDto> tracking = order.getTrackingHistory().stream()
                                .sorted(Comparator.comparing(OrderTracking::getCreatedAt))
                                .map(this::toTracking)
                                .toList();

                return new OrderDetailsDto(
                                order.getOrderId(),
                                order.getStatus(),
                                order.getOrderDate(),
                                order.getSubtotal(),
                                order.getFee(),
                                order.getDiscountValue(),
                                order.getTotal(),
                                order.getNote(),
                                order.getCustomerFullName(),
                                order.getRestaurantBranchName(),
                                address,
                                order.getCouponAmount(),
                                items,
                                tracking);
        }

        private OrderItemDto toOrderItem(OrderItem item) {
                return new OrderItemDto(
                                item.getId(),
                                item.getMenuItem().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubtotal(),
                                item.getNote());
        }

        private OrderTrackingDto toTracking(OrderTracking tracking) {
                return new OrderTrackingDto(
                                tracking.getStatus(),
                                tracking.getDescription(),
                                tracking.getCreatedAt());
        }
}
