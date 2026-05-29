package com.mentorship.food_delivery_app.customer.controller;


import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @DeleteMapping("/account")
    public ResponseEntity<Void> deactivateAccount() {
        customerService.deactivateAccount();
        return ResponseEntity.noContent().build();
    }
}
