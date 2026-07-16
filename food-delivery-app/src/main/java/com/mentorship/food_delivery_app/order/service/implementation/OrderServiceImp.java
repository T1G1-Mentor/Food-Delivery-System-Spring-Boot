package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.common.dto.EmailEventRecord;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.exceptions.CancelledOrderException;
import com.mentorship.food_delivery_app.order.exceptions.DeliveredOrderException;
import com.mentorship.food_delivery_app.order.exceptions.OrderNotFoundException;
import com.mentorship.food_delivery_app.order.handler.*;
import com.mentorship.food_delivery_app.order.mapper.OrderMapper;
import com.mentorship.food_delivery_app.order.repository.OrderItemRepository;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.order.service.contract.OrderTrackingService;
import com.mentorship.food_delivery_app.payment.service.PaymentService;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImp implements OrderService {
    private final OrderItemRepository orderItemRepository;
    @Qualifier("dbExecuter")
    private final Executor dbExecuter;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final RestaurantService restaurantService;
    private final CartService cartService;
    private final OrderTrackingService orderTrackingService;
    private final OrderMapper orderMapper;


    @Transactional
    @Override
    public OrderResponseDto placeOrder(PlaceOrderRequestDto request, UUID customerId) {

        OrderProcessingContext orderProcessingContext = buildContext(request, customerId);

        OrderHandler handler = OrderHandler.processOrder(
                // validate cart exists, unlocked, and current restaurant matches the request's restaurant
                new CartValidationHandler(),
//                validate that the restaurant is in it's working hours
                new RestaurantOpenTimeValidationHandler(),
//                validate that all menu items are available, and they belong to the same restaurant
                new MenuItemValidationHandler(),
//                finalizing the order, creating new tracking in pending status until the payment is processed and the restaurants accepts the order.
                new FinalizeOrderHandler(orderRepository, orderMapper),
//                Processing a dummy payment
                new PaymentProcessHandler(paymentService)
        );
        OrderResponseDto orderResponse = handler.handle(orderProcessingContext);

        this.notifyOrderPlaced(orderProcessingContext.getCustomerEmail(),
                orderResponse.orderId());

        return orderResponse;
    }


    @Transactional
    @Override
    public void cancelOrder(UUID orderId, UUID userId) {
        Order order = getAndValidateOrder(orderId, userId);

        if (order.isCancelled())
            throw new CancelledOrderException(ErrorMessage.ORDER_ALREADY_CANCELLED.getMessage());

        OrderStatus status = OrderStatus.CANCELLED;

        OrderTracking.createNewOrderTracking(status, status.getDescription(), order);

        sendStatusUpdateEmail(order.getCustomerEmail(), status.getDescription());


    }

    @Transactional
    @Override
    public void handlerOrderStatusUpdate(UUID orderId, UUID userId) {

        Order order = getAndValidateOrder(orderId, userId);
        OrderStatus newStatus = getNextStatus(order.getStatus());

        OrderTracking.createNewOrderTracking(newStatus, newStatus.getDescription(), order);

        sendStatusUpdateEmail(order.getCustomerEmail(), newStatus.getDescription());

    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderListItemDto> listOrders(UUID restaurantBranchId, OrderStatus status, Pageable pageable) {
        log.debug("Fetching orders for branch ID: {} with status filter: {}", restaurantBranchId, status);

        Page<Order> orders = (status != null)
                ? orderRepository.findOrdersByBranchIdAndStatus(restaurantBranchId, status, pageable)
                : orderRepository.findOrdersByBranchId(restaurantBranchId, pageable);

        log.info("Fetched {} orders for branch ID: {}", orders.getTotalElements(), restaurantBranchId);
        return orders.map(orderMapper::toListItem);
    }

    @Override
    public OrderDetailsDto getOrderDetails(UUID orderId, CustomerPrincipal customerPrincipal) {

        log.debug("Fetching order details for Order ID: {} by customer user ID: {}", orderId, customerPrincipal.getUserId());
//        Ownership check
        Order order = getOrderWithRestaurantById(orderId, customerPrincipal);

        List<OrderTracking> orderTracking;
        List<OrderItem> orderItems;

        CompletableFuture<List<OrderTracking>> orderTrackingListFuture = supplyFuture(getOrderTrackingByOrderId(orderId));

        CompletableFuture<List<OrderItem>> orderItemListFuture = supplyFuture(orderItemRepository.findByOrderId(orderId));

        handleFutures(orderTrackingListFuture, orderItemListFuture);

        orderTracking = orderTrackingListFuture.join();
        orderItems = orderItemListFuture.join();

        log.info("Successfully fetched details for Order ID: {}", orderId);
        return orderMapper.toDetails(order, orderItems, orderTracking,
                customerPrincipal.getCustomerFullName());

    }

    private @NonNull Order getOrderWithRestaurantById(UUID orderId, CustomerPrincipal customerPrincipal) {
        return orderRepository.findOrderWithRestaurantBranchByIdAndCustomerId(orderId, customerPrincipal.getCustomerId())
                .orElseThrow(() ->
                        new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderListItemDto> getCustomerOrderHistory(OrderStatus status, Pageable pageable, CustomerPrincipal customerPrincipal) {

        log.debug("Fetching order history for customer ID: {} with status filter: {}", customerPrincipal.getCustomerId(), status);

        Page<Order> orders = (status != null)
                ? orderRepository.findOrdersByCustomerIdAndStatus(customerPrincipal.getCustomerId(), status, pageable)
                : orderRepository.findOrdersByCustomerId(customerPrincipal.getCustomerId(), pageable);

        log.info("Fetched {} orders in history for user ID: {}", orders.getTotalElements(), customerPrincipal.getCustomerId());
        return orders.map(order -> orderMapper.toHistoryItem(order, customerPrincipal.getCustomerFullName()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderTrackingDto> getOrderTrackingHistory(UUID customerId, UUID orderId) {
        return orderTrackingService.getTrackingHistory(customerId, orderId);
    }

    private <T> CompletableFuture<T> supplyFuture(T process) {
        return CompletableFuture.supplyAsync(() -> process, dbExecuter);
    }

    private void handleFutures(CompletableFuture<List<OrderTracking>> orderTrackingListFuture,
                               CompletableFuture<List<OrderItem>> orderItemListFuture) {
        try {
            CompletableFuture.allOf(orderTrackingListFuture, orderItemListFuture).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException re) {
                throw re;
            }
            throw new IllegalStateException("Unexpected error fetching order details", e.getCause());
        }


    }

    private List<OrderTracking> getOrderTrackingByOrderId(UUID orderId) {
        return orderTrackingService.getByOrderId(orderId);
    }

    private Order getAndValidateOrder(UUID orderId, UUID userId) {
        log.debug("Validating authorization and fetching Order ID: {} for User ID: {}", orderId, userId);

        return orderRepository.findOrderByIdAndAdminId(orderId, userId)
                .orElseThrow(() -> {
                    log.warn("Order validation failed. Order ID: {} not found or User ID: {} is not authorized", orderId, userId);
                    return new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage());
                });
    }


    private OrderStatus getNextStatus(OrderStatus status) {
        return switch (status) {
            case PENDING -> OrderStatus.IN_PROGRESS;
            case IN_PROGRESS -> OrderStatus.ON_THE_WAY;
            case ON_THE_WAY -> OrderStatus.DELIVERED;
            case DELIVERED -> throw new DeliveredOrderException(ErrorMessage.ORDER_ALREADY_DELIVERED.getMessage());
            case CANCELLED -> throw new CancelledOrderException(ErrorMessage.ORDER_ALREADY_CANCELLED.getMessage());
        };

    }

    //    dummy template
    private void sendStatusUpdateEmail(String email, String staus) {
        EmailEventRecord emailEventRecord = new EmailEventRecord(email,
                "Order Status Update", String.format
                ("Your order status just got updated, %s", staus));
        publisher.publishEvent(emailEventRecord);
    }

    private OrderProcessingContext buildContext(PlaceOrderRequestDto request, UUID customerId) {
        return new OrderProcessingContext(
                request,
                () -> cartService.getCartByIdAndCustomerIdWithLock(request.cartId(), customerId),// cart fetch and lock supplier
                () -> cartService.getCartItemsWithDetails(request.cartId()), // cart items supplier
                () -> customerService.getCustomerReference(customerId), // customer supplier
                () -> restaurantService.getRestaurantCoupon(request.couponId()) // coupon supplier
        );
    }

    private void notifyOrderPlaced(String customerEmail, UUID orderId) {
        EmailEventRecord emailEventRecord = new EmailEventRecord(customerEmail,
                "Order Confirmation",
                "Your order has been placed. Order ID: " + orderId);
        publisher.publishEvent(emailEventRecord);


    }

}
