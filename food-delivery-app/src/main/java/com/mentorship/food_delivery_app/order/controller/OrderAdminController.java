package com.mentorship.food_delivery_app.order.controller;

import com.mentorship.food_delivery_app.order.dto.response.OrderListItemDto;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderService orderService;


    @PostMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID orderId) {
        orderService.handlerOrderStatusUpdate(orderId);
        return ResponseEntity.noContent().build();
    }

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
}
