package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.cart.service.contract.CartService;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceUnavailableException;
import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.dto.OrderPricing;
import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.entity.DeliveryAddress;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.mapper.OrderMapper;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.payment.service.PaymentService;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final RestaurantService restaurantService;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    @Value("${app.test.user-id}")
    private String userId;

    @Transactional
    @Override
    public OrderResponseDto placeOrder(PlaceOrderRequestDto request) {
        Customer customer = customerService.fetchCustomerWithCartInfoByUserId(UUID.fromString(userId));
        Cart cart = validateCartExists(customer);
        cartService.lockCart(cart.getId());

//        validateRestaurantIsOpen(cart);
        validateCartItemsAvailability(cart);

        Coupon coupon = restaurantService.getRestaurantCoupon(request.couponId(), cart.getCurrentRestaurant());
        OrderPricing pricing = OrderPricing.calculate(cart, coupon);

        paymentService.processPayment(); // dummy template for processing payment

        Order savedOrder = createAndPersistOrder(customer, cart, pricing, request.deliveryAddress());
        notifyOrderPlaced(savedOrder);

        return orderMapper.toResponse(savedOrder);
    }


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

    private void validateRestaurantIsOpen(Cart cart) {
        RestaurantBranch branch = cart.getCurrentRestaurant();
        if (!branch.isOpen()) {
            log.error("Restaurant {} is closed", branch.getId());
            throw new ResourceUnavailableException(ErrorMessage.RESTAURANT_CLOSED.getMessage());
        }
    }

    private Cart validateCartExists(Customer customer) {
        Cart cart = customer.getCart();
        if (cart == null) {
            log.error("Cart not found for user {}", userId);
            throw new ResourceNotFoundException(ErrorMessage.CART_NOT_FOUND.getMessage());
        }
        return cart;
    }

    private void validateCartItemsAvailability(Cart cart) {
        Set<CartItem> unavailableItems = cart.getUnavailableItems();
        if (!unavailableItems.isEmpty()) {
            List<String> unavailableItemNames = unavailableItems.stream()
                    .map(item -> item.getMenuItem().getName())
                    .toList();
            log.error("Unavailable cart items: {}", unavailableItemNames);
            throw new ResourceUnavailableException("Some items in the cart are not available: " + unavailableItemNames);
        }
    }

    private Order createAndPersistOrder(Customer customer, Cart cart, OrderPricing pricing, DeliveryAddressDto deliveryAddress) {
        RestaurantBranch branch = cart.getCurrentRestaurant();

        DeliveryAddress deliveryAddressToPersist = resolveDeliveryAddress(customer, deliveryAddress);

        Order order = Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddressToPersist)
                .branch(branch)
                .subtotal(pricing.subtotal())
                .total(pricing.total())
                .fee(pricing.deliveryFee())
                .discountValue(pricing.discount())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);
        savedOrder.setItems(buildOrderItems(cart, savedOrder));

        createNewOrderTracking(OrderStatus.PENDING, OrderStatus.PENDING.getDescription(), savedOrder);

        return savedOrder;
    }

    private Set<OrderItem> buildOrderItems(Cart cart, Order order) {
        return cart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .menuItem(cartItem.getMenuItem())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getMenuItem().getPrice())
                        .subtotal(cartItem.getTotalPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toSet());
    }

    private DeliveryAddress resolveDeliveryAddress(Customer customer, DeliveryAddressDto deliveryAddressDto) {
        if (deliveryAddressDto == null) {
            return DeliveryAddress.from(customer.getDefaultAddress());
        }
        return DeliveryAddress.from(deliveryAddressDto);
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

    @Transactional(readOnly = true)
    @Override
    public OrderDetailsDto getOrderDetails(UUID orderId) {
        UUID userId = userService.getDummyLoggedInUser().getId();
        log.debug("Fetching order details for Order ID: {} by customer user ID: {}", orderId, userId);

        Order order = orderRepository.fetchOrderDetailsForCustomer(orderId, userId)
                .orElseThrow(() -> {
                    log.warn("Order details not found. Order ID: {} for user ID: {}", orderId, userId);
                    return new ResourceNotFoundException(ErrorMessage.ORDER_NOT_FOUND.getMessage());
                });

        log.info("Successfully fetched details for Order ID: {}", orderId);
        return orderMapper.toDetails(order);
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

        return orderRepository.findOrderByIdAndAdminId(orderId, user.getId())
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

    private void notifyOrderPlaced(Order order) {
        try {
            emailService.sendEmailAsync(
                    order.getCustomer().getUser().getEmail(),
                    "Order Confirmation",
                    "Your order has been placed. Order ID: " + order.getOrderId()
            );
        } catch (Throwable e) {
            log.warn("Failed to send order confirmation email for order {}", order.getOrderId(), e);
        }
    }

}
