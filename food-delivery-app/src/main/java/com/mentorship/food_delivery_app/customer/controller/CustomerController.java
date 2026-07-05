package com.mentorship.food_delivery_app.customer.controller;


import com.mentorship.food_delivery_app.customer.dto.request.PreferredPaymentRequest;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.service.contract.OrderService;
import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> deactivateAccount(@AuthenticationPrincipal CustomerPrincipal customer) {
        customerService.deactivateAccount(customer.getCustomerId(), customer.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{orderId}/tracking-history")
    public ResponseEntity<List<OrderTrackingDto>> getCustomerOrderTrackingHistory(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal CustomerPrincipal customer
    ) {
        return ResponseEntity.
                ok(orderService.getOrderTrackingHistory(customer.getCustomerId(),
                        orderId));
    }

    @PatchMapping("/preferred-payment")
    public ResponseEntity<Void> addCustomerPreferredPayment(@RequestBody @Valid PreferredPaymentRequest preferredPayment,
                                                            @AuthenticationPrincipal CustomerPrincipal customer) {

        customerService.addCustomerPreferredPaymentType(preferredPayment.preferredPayment()
                , customer.getCustomerId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preferred-payment")
    public ResponseEntity<String> getCustomerPreferredPayment(@AuthenticationPrincipal CustomerPrincipal customer) {

        return ResponseEntity.ok(
                customerService.getCustomerPreferredPaymentType(customer.getCustomerId())
        );
    }
}
