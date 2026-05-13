package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    @Override
    public void cancelOrder(UUID orderId) {
        Order order =getAndValidateOrder(orderId);

        if (order.isCancelled())
            throw new BadRequestException(ErrorMessage.ORDER_ALREADY_CANCELLED.getMessage());

        OrderStatus status=OrderStatus.CANCELLED;
        log.info("Initiating status update for Order ID: {} to Status: {}", orderId, status);

        createNewOrderTracking(status,status.getDescription(),order);

        log.debug("Dispatching asynchronous status update email to: {}", order.getCustomerEmail());
        sendStatusUpdateEmail(order.getCustomerEmail(), status.getDescription());

        log.info("Successfully completed status update for Order ID: {}", orderId);

    }

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
