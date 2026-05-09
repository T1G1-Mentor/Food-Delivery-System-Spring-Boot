package com.mentorship.food_delivery_app.order.controller;

import com.mentorship.food_delivery_app.order.dto.request.UpdateOrderStatusRequestDto;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(@Valid @RequestBody UpdateOrderStatusRequestDto request){
        orderService.updateOrderStatus(request);
        return ResponseEntity.noContent().build();
    }
}
