package com.mentorship.food_delivery_app.services.order;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.services.contract.EmailService;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.order.service.implementation.OrderServiceImp;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelOrderTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;


    @InjectMocks
    private OrderServiceImp orderService;


    private Order order;
    private UUID orderId;
    private UUID userId;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        userId = UUID.randomUUID();


        dummyUser = new User();
        dummyUser.setId(userId);

        Customer customer = new Customer();
        User user = new User();
        user.setEmail("customer@example.com");
        customer.setUser(user);

        order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(customer);
        order.setTrackingHistory(new HashSet<>());

    }

    @Test
    @DisplayName("""
            GIVEN: valid request and authorization
            AND: order status is Pending
            WHEN: cancelOrder is called
            THEN: order is cancelled, tracking is appended to the Set, and email is sent
            """)
    void cancelOrder_ShouldCancelOrderAndSendEmail_WhenStatusIsPending() {
        order.setStatus(OrderStatus.PENDING);

        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);


        assertThat(order.getTrackingHistory())
                .hasSize(1)
                .singleElement()
                .satisfies(appendedTracking -> {
                    assertThat(appendedTracking.getStatus()).isEqualTo(OrderStatus.CANCELLED);
                    assertThat(appendedTracking.getDescription()).isEqualTo(OrderStatus.CANCELLED.getDescription());
                    assertThat(appendedTracking.getOrder()).isEqualTo(order); // Validates Bidirectional mapping!
                });

        // Assert - 3. Verify External Service Calls
        verify(emailService).sendEmailAsync(
                eq(order.getCustomerEmail()),
                eq("Order Status Update"),
                contains(OrderStatus.CANCELLED.getExposableName())
        );
        verify(emailService, times(1)).sendEmailAsync(any(), any(), any());
    }

    @Test
    @DisplayName("""
            GIVEN: valid request and authorization
            AND: order status is In Progress
            WHEN: cancelOrder is called
            THEN: order is cancelled, tracking is appended to the Set, and email is sent
            """)
    void cancelOrder_ShouldCancelOrderAndSendEmail_WhenStatusIsInProgress() {
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);


        assertThat(order.getTrackingHistory())
                .hasSize(1)
                .singleElement()
                .satisfies(appendedTracking -> {
                    assertThat(appendedTracking.getStatus()).isEqualTo(OrderStatus.CANCELLED);
                    assertThat(appendedTracking.getDescription()).isEqualTo(OrderStatus.CANCELLED.getDescription());
                    assertThat(appendedTracking.getOrder()).isEqualTo(order); // Validates Bidirectional mapping!
                });

        // Assert - 3. Verify External Service Calls
        verify(emailService).sendEmailAsync(
                eq(order.getCustomerEmail()),
                eq("Order Status Update"),
                contains(OrderStatus.CANCELLED.getExposableName())
        );
        verify(emailService, times(1)).sendEmailAsync(any(), any(), any());
    }

    @Test
    @DisplayName("""
            GIVEN: valid request and authorization
            AND: order status is On The Way
            WHEN: cancelOrder is called
            THEN: order is cancelled, tracking is appended to the Set, and email is sent
            """)
    void cancelOrder_ShouldCancelOrderAndSendEmail_WhenStatusIsOnTheWay() {
        order.setStatus(OrderStatus.ON_THE_WAY);

        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);


        assertThat(order.getTrackingHistory())
                .hasSize(1)
                .singleElement()
                .satisfies(appendedTracking -> {
                    assertThat(appendedTracking.getStatus()).isEqualTo(OrderStatus.CANCELLED);
                    assertThat(appendedTracking.getDescription()).isEqualTo(OrderStatus.CANCELLED.getDescription());
                    assertThat(appendedTracking.getOrder()).isEqualTo(order); // Validates Bidirectional mapping!
                });

        // Assert - 3. Verify External Service Calls
        verify(emailService).sendEmailAsync(
                eq(order.getCustomerEmail()),
                eq("Order Status Update"),
                contains(OrderStatus.CANCELLED.getExposableName())
        );
        verify(emailService, times(1)).sendEmailAsync(any(), any(), any());
    }

    @Test
    @DisplayName("""
            GIVEN: valid request and authorization
            AND: order status is Delivered
            WHEN: cancelOrder is called
            THEN: order is cancelled, tracking is appended to the Set, and email is sent
            """)
    void cancelOrder_ShouldCancelOrderAndSendEmail_WhenStatusIsDelivered() {
        order.setStatus(OrderStatus.DELIVERED);

        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);


        assertThat(order.getTrackingHistory())
                .hasSize(1)
                .singleElement()
                .satisfies(appendedTracking -> {
                    assertThat(appendedTracking.getStatus()).isEqualTo(OrderStatus.CANCELLED);
                    assertThat(appendedTracking.getDescription()).isEqualTo(OrderStatus.CANCELLED.getDescription());
                    assertThat(appendedTracking.getOrder()).isEqualTo(order); // Validates Bidirectional mapping!
                });

        // Assert - 3. Verify External Service Calls
        verify(emailService).sendEmailAsync(
                eq(order.getCustomerEmail()),
                eq("Order Status Update"),
                contains(OrderStatus.CANCELLED.getExposableName())
        );
        verify(emailService, times(1)).sendEmailAsync(any(), any(), any());
    }

    @Test
    @DisplayName("""
            GIVEN: Trying to cancel order which already been cancelled
            WHEN: cancelOrder is called
            THEN: BadRequestException is thrown and no side effects occur
            """)
    void cancelOrder_ShouldThrowException_WhenTryingToCancelACancelledOrder() {
        order.setStatus(OrderStatus.CANCELLED);
        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(orderId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorMessage.ORDER_ALREADY_CANCELLED.getMessage());

        verifyNoInteractions(emailService);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getTrackingHistory()).isEmpty();
    }
}
