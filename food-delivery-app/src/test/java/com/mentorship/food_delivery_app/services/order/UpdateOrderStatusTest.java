package com.mentorship.food_delivery_app.services.order;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.BadRequestException;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusTest {

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
        order.setStatus(OrderStatus.PENDING);
        order.setTrackingHistory(new HashSet<>());
        order.setStatus(OrderStatus.PENDING);

    }

    @Test
    @DisplayName("""
            GIVEN: valid request and authorization
            WHEN: updateOrderStatus is called
            THEN: state is updated, tracking is appended to the Set, and email is sent
            """)
    void updateOrderStatus_ShouldUpdateStatusAndSendEmail_WhenValid() {
        String exposableName = OrderStatus.IN_PROGRESS.getExposableName();

        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);


        assertThat(order.getTrackingHistory())
                .hasSize(1)
                .singleElement()
                .satisfies(appendedTracking -> {
                    assertThat(appendedTracking.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
                    assertThat(appendedTracking.getDescription()).isEqualTo(OrderStatus.IN_PROGRESS.getDescription());
                    assertThat(appendedTracking.getOrder()).isEqualTo(order); // Validates Bidirectional mapping!
                });

        // Assert - 3. Verify External Service Calls
        verify(emailService).sendEmailAsync(
                eq(order.getCustomerEmail()),
                eq("Order Status Update"),
                contains(exposableName)
        );
        verify(emailService, times(1)).sendEmailAsync(any(), any(), any());
    }

    @Test
    @DisplayName("""
            GIVEN: invalid order or unauthorized user
            WHEN: updateOrderStatus is called
            THEN: ResourceNotFoundException is thrown and no side effects occur
            """)
    void updateOrderStatus_ShouldThrowException_WhenOrderNotFoundOrUnauthorized() {
        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderStatus(orderId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ErrorMessage.ORDER_NOT_FOUND.getMessage());

        verifyNoInteractions(emailService);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTrackingHistory()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN: Trying to update order which already been delivered
            WHEN: updateOrderStatus is called
            THEN: BadRequestException is thrown and no side effects occur
            """)
    void updateOrderStatus_ShouldThrowException_WhenTryingToUpdateOrderAlreadyDelivered() {
        order.setStatus(OrderStatus.DELIVERED);
        when(userService.getDummyLoggedInUser()).thenReturn(dummyUser);
        when(orderRepository.fetchOrderWithTrackingAndRestaurantBranchAndCustomer(orderId, userId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(orderId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorMessage.ORDER_ALREADY_DELIVERED.getMessage());

        verifyNoInteractions(emailService);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(order.getTrackingHistory()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN: Trying to update order which already been cancelled
            WHEN: updateOrderStatus is called
            THEN: BadRequestException is thrown and no side effects occur
            """)
    void updateOrderStatus_ShouldThrowException_WhenTryingToUpdateOrderAlreadyCancelled() {
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
