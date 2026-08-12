package com.mentorship.food_delivery_app.order.controller;

import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderDetailsDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Order management")
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
            @PageableDefault(size = 10, sort = "orderDate") Pageable pageable,
            @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {
        return ResponseEntity.ok(orderService.getCustomerOrderHistory(status, pageable, customerPrincipal));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable UUID orderId, @AuthenticationPrincipal CustomerPrincipal customerPrincipal) {

        return ResponseEntity.ok(orderService.getOrderDetails(orderId, customerPrincipal));
    }
}
