package com.mentorship.food_delivery_app.customer.controller;


import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    @DeleteMapping("/account")
    public ResponseEntity<Void> deactivateAccount() {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id
//        the principal will also contain the user-Id
        customerService.deactivateAccount(customer.getId(), customer.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{orderId}/tracking-history")
    public ResponseEntity<List<OrderTrackingDto>> getCustomerOrderTrackingHistory(
            @PathVariable UUID orderId
            )
    {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id
        return ResponseEntity.
                ok(orderService.getOrderTrackingHistory(customer.getId(),
                orderId));
    }
}
