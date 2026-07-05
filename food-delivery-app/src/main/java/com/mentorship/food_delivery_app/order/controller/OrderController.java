package com.mentorship.food_delivery_app.order.controller;

import java.util.UUID;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1/customers/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller",description = "Order management")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid PlaceOrderRequestDto request,
                                                       @AuthenticationPrincipal CustomerPrincipal customer) {

        OrderResponseDto order = orderService.placeOrder(request, customer.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }



    @GetMapping("/history")
    public ResponseEntity<Page<OrderListItemDto>> getCustomerOrderHistory(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        return ResponseEntity.ok(orderService.getCustomerOrderHistory(status, pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }
}
