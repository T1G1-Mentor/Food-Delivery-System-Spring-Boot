package com.mentorship.food_delivery_app.order.mapper;

import com.mentorship.food_delivery_app.order.dto.response.*;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OrderMapper {

    public OrderListItemDto toListItem(Order order) {
        String customerFullName = order.getCustomer().getUser().getFirstName()
                + " " + order.getCustomer().getUser().getLastName();
        String restaurantName = order.getBranch().getRestaurant().getName();

        return new OrderListItemDto(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotal(),
                customerFullName,
                restaurantName
        );
    }

    public OrderListItemDto toHistoryItem(Order order) {
        String restaurantName = order.getBranch().getRestaurant().getName();

        return new OrderListItemDto(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotal(),
                null,
                restaurantName
        );
    }

    public OrderDetailsDto toDetails(Order order) {
        String customerFullName = order.getCustomer().getUser().getFirstName()
                + " " + order.getCustomer().getUser().getLastName();
        String restaurantName = order.getBranch().getRestaurant().getName();

        AddressDto address = new AddressDto(
                order.getAddress().getLabel(),
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getBuilding(),
                order.getAddress().getApartment()
        );

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
                customerFullName,
                restaurantName,
                address,
                order.getCoupon() != null ? order.getCoupon().getAmount() : null,
                items,
                tracking
        );
    }

    public OrderSummaryDto toSummary(Order order) {
        String customerFullName = order.getCustomer().getUser().getFirstName()
                + " " + order.getCustomer().getUser().getLastName();
        String restaurantName = order.getBranch().getRestaurant().getName();

        return new OrderSummaryDto(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderDate(),
                order.getSubtotal(),
                order.getFee(),
                order.getDiscountValue(),
                order.getTotal(),
                order.getItems().size(),
                customerFullName,
                restaurantName
        );
    }

    private OrderItemDto toOrderItem(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal(),
                item.getNote()
        );
    }

    private OrderTrackingDto toTracking(OrderTracking tracking) {
        return new OrderTrackingDto(
                tracking.getStatus(),
                tracking.getDescription(),
                tracking.getCreatedAt()
        );
    }
}
