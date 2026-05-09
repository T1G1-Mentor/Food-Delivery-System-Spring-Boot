package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import com.mentorship.food_delivery_app.order.dto.request.UpdateOrderStatusRequestDto;
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
    public void updateOrderStatus(UpdateOrderStatusRequestDto request) {
        log.info("Initiating status update for Order ID: {} to Status: {}", request.orderId(), request.status());

        Order order = getAndValidateOrder(request.orderId());

        createNewOrderTracking(request.status(), request.description(), order);

        log.debug("Dispatching asynchronous status update email to: {}", order.getCustomerEmail());
        sendStatusUpdateEmail(order.getCustomerEmail(), request.status().getExposableName());

        log.info("Successfully completed status update for Order ID: {}", request.orderId());
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

    //    dummy template
    private void sendStatusUpdateEmail(String email, String staus) {
        emailService.sendEmailAsync(email, "Order Status Update", String.format
                ("Your order status just got updated, and its now %s.", staus));
    }

}
