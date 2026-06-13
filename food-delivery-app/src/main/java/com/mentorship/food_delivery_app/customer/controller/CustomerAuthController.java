package com.mentorship.food_delivery_app.customer.controller;

import com.mentorship.food_delivery_app.customer.dto.request.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers/auth")
@RequiredArgsConstructor
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerRegistrationDto customerRegistrationDto) {

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(
                        customerAuthService.register(customerRegistrationDto)
                );
    }
}
