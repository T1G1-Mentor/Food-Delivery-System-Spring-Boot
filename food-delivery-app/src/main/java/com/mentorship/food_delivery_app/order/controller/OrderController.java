package com.mentorship.food_delivery_app.order.controller;

import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

//    @PreAuthorize("hasRole('ADMIN')") // spring security is not enabled yet
    @PostMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID orderId) {
        orderService.updateOrderStatus(orderId);
        return ResponseEntity.noContent().build();
    }

    //    @PreAuthorize("hasRole('ADMIN')") // spring security is not enabled yet
    @DeleteMapping("/{orderId}/status")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
