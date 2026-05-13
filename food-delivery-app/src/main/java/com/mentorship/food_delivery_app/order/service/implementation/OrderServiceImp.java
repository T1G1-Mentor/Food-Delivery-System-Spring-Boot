package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderSummaryDto;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.mapper.OrderMapper;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public void updateOrderStatus(UUID orderId) {

        Order order = getAndValidateOrder(orderId);
        OrderStatus newStatus = getNextStatus(order.getStatus());

        log.info("Initiating status update for Order ID: {} to Status: {}", orderId, newStatus);

        createNewOrderTracking(newStatus, newStatus.getDescription(), order);

        log.debug("Dispatching asynchronous status update email to: {}", order.getCustomerEmail());
        sendStatusUpdateEmail(order.getCustomerEmail(), newStatus.getDescription());

        log.info("Successfully completed status update for Order ID: {}", orderId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderListItemDto> listOrders(OrderStatus status, Pageable pageable) {
        UUID adminId = userService.getDummyLoggedInUser().getId();
        log.debug("Fetching orders for admin ID: {} with status filter: {}", adminId, status);

        Page<Order> orders = (status != null)
                ? orderRepository.findOrdersByAdminAndStatus(adminId, status, pageable)
                : orderRepository.findOrdersByAdmin(adminId, pageable);

        log.info("Fetched {} orders for admin ID: {}", orders.getTotalElements(), adminId);
        return orders.map(orderMapper::toListItem);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDetailsDto getOrderDetails(UUID orderId) {
        UUID adminId = userService.getDummyLoggedInUser().getId();
        log.debug("Fetching order details for Order ID: {} by admin ID: {}", orderId, adminId);

        Order order = orderRepository.fetchOrderDetailsForAdmin(orderId, adminId)
                .orElseThrow(() -> {
                    log.warn("Order details not found. Order ID: {} for admin ID: {}", orderId, adminId);
                    return new ResourceNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage());
                });

        log.info("Successfully fetched details for Order ID: {}", orderId);
        return orderMapper.toDetails(order);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderSummaryDto getOrderSummary(UUID orderId) {
        UUID adminId = userService.getDummyLoggedInUser().getId();
        log.debug("Fetching order summary for Order ID: {} by admin ID: {}", orderId, adminId);

        Order order = orderRepository.fetchOrderSummaryForAdmin(orderId, adminId)
                .orElseThrow(() -> {
                    log.warn("Order summary not found. Order ID: {} for admin ID: {}", orderId, adminId);
                    return new ResourceNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage());
                });

        log.info("Successfully fetched summary for Order ID: {}", orderId);
        return orderMapper.toSummary(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderListItemDto> getCustomerOrderHistory(OrderStatus status, Pageable pageable) {
        UUID userId = userService.getDummyLoggedInUser().getId();
        log.debug("Fetching order history for user ID: {} with status filter: {}", userId, status);

        Page<Order> orders = (status != null)
                ? orderRepository.findOrdersByUserIdAndStatus(userId, status, pageable)
                : orderRepository.findOrdersByUserId(userId, pageable);

        log.info("Fetched {} orders in history for user ID: {}", orders.getTotalElements(), userId);
        return orders.map(orderMapper::toHistoryItem);
    }

    private Order getAndValidateOrder(UUID orderId) {
        User user = userService.getDummyLoggedInUser();
        log.debug("Validating authorization and fetching Order ID: {} for User ID: {}", orderId, user.getId());

        return orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, user.getId())
                .orElseThrow(() -> {
                    log.warn("Order validation failed. Order ID: {} not found or User ID: {} is not authorized", orderId, user.getId());
                    return new ResourceNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage());
                });
    }

    private void createNewOrderTracking(OrderStatus status, String description, Order order) {
        log.debug("Appending new tracking event to Order ID: {}. Status: {}", order.getOrderId(), status);

        OrderTracking tracking = OrderTracking.
                builder()
                .description(description)
                .status(status)
                .build();

        order.addTrackingEvent(tracking);
        order.setStatus(status);

    }

    private OrderStatus getNextStatus(OrderStatus status) {
        return switch (status) {
            case PENDING -> OrderStatus.IN_PROGRESS;
            case IN_PROGRESS -> OrderStatus.ON_THE_WAY;
            case ON_THE_WAY -> OrderStatus.DELIVERED;
            case DELIVERED -> throw new BadRequestException(ErrorMessage.ORDER_ALREADY_DELIVERED.getMessage());
            case CANCELLED -> throw new BadRequestException(ErrorMessage.ORDER_ALREADY_CANCELLED.getMessage());
        };

    }

    //    dummy template
    private void sendStatusUpdateEmail(String email, String staus) {
        emailService.sendEmailAsync(email, "Order Status Update", String.format
                ("Your order status just got updated, %s",staus));
    }

}
