package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import com.mentorship.food_delivery_app.order.dto.response.*;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderTrackingMapper orderTrackingMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderResponseDto toResponse(Order order) {
        List<OrderItemResponseDto> orderItems= order.getItems().stream().map(orderItemMapper::toResponse).toList();
        return OrderResponseDto.builder().orderId(order.getOrderId())
                .restaurantBranchId(order.getBranch().getRestaurantBranchId())
                .restaurantName(order.getRestaurantBranchName())
                .orderSubtotal(order.getSubtotal())
                .orderFee(order.getFee())
                .orderDiscount(order.getDiscountValue())
                .orderTotal(order.getTotal())
                .orderDeliveryAddress(DeliveryAddressDto.from(order.getDeliveryAddress()))
                .orderItems(orderItems).build();
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

    public OrderListItemDto toHistoryItem(Order order, String customerFullName) {
        return new OrderListItemDto(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotal(),
                customerFullName,
                order.getRestaurantBranchName());
    }

    public OrderDetailsDto toDetails(Order order, List<OrderItem> orderItems, List<OrderTracking> orderTrackings, String customerFullName) {
        AddressDto address = new AddressDto(
                order.getDeliveryCity(),
                order.getDeliveryStreet(),
                order.getDeliveryBuilding(),
                order.getDeliveryApartment());

        List<OrderItemDto> items = orderItems.stream()
                .map(this::toOrderItem)
                .toList();

        List<OrderTrackingDto> tracking = orderTrackings.stream()
                .sorted(Comparator.comparing(OrderTracking::getCreatedAt))
                .map(orderTrackingMapper::toResponse)
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
                customerFullName,
                order.getRestaurantBranchName(),
                address,
                order.getCouponAmount(),
                items,
                tracking);
    }

    private OrderItemDto toOrderItem(OrderItem item) {
        return new OrderItemDto(
                item.getOrderItemId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal(),
                item.getNote());
    }


}
