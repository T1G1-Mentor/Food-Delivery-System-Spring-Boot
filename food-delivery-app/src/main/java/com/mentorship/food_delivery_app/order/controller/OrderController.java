package com.mentorship.food_delivery_app.order.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid PlaceOrderRequestDto request) {
        OrderResponseDto order = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    // @PreAuthorize("hasRole('ADMIN')") // spring security is not enabled yet
    @PostMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID orderId) {
        orderService.updateOrderStatus(orderId);
        return ResponseEntity.noContent().build();
    }

    // @PreAuthorize("hasRole('ADMIN')") // spring security is not enabled yet
    @DeleteMapping("/{orderId}/status")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{restaurantBranchId}")
    public ResponseEntity<Page<OrderListItemDto>> listOrders(
            @PathVariable UUID restaurantBranchId,
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        return ResponseEntity.ok(orderService.listOrders(restaurantBranchId, status, pageable));
    }

    @GetMapping("/customer/history")
    public ResponseEntity<Page<OrderListItemDto>> getCustomerOrderHistory(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        return ResponseEntity.ok(orderService.getCustomerOrderHistory(status, pageable));
    }

    @GetMapping("/customer/{orderId}")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }
}
