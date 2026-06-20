package com.mentorship.food_delivery_app.customer.controller;


import com.mentorship.food_delivery_app.customer.dto.request.PreferredPaymentRequest;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Controller", description = "For customers to manage their accounts and get information related to their activity")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    @DeleteMapping("/account")
    public ResponseEntity<Void> deactivateAccount() {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id
//        the principal will also contain the user-Id
        customerService.deactivateAccount(customer.getCustomerId(), customer.getUser().getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{orderId}/tracking-history")
    public ResponseEntity<List<OrderTrackingDto>> getCustomerOrderTrackingHistory(
            @PathVariable UUID orderId
    ) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id
        return ResponseEntity.
                ok(orderService.getOrderTrackingHistory(customer.getCustomerId(),
                        orderId));
    }

    @PatchMapping("/preferred-payment")
    public ResponseEntity<Void> addCustomerPreferredPayment(@RequestBody @Valid PreferredPaymentRequest preferredPayment) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        customerService.addCustomerPreferredPaymentType(preferredPayment.preferredPayment()
                , customer.getCustomerId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preferred-payment")
    public ResponseEntity<String> getCustomerPreferredPayment() {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        return ResponseEntity.ok(
                customerService.getCustomerPreferredPaymentType(customer.getCustomerId())
        );
    }
}
