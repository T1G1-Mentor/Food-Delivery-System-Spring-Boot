package com.mentorship.food_delivery_app.auth.controller;

import com.mentorship.food_delivery_app.auth.dto.LoginDto;
import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.auth.service.contract.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "For customer registration, and both Admin, and Customer login")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto){
       return ResponseEntity.
                ok(authService.login(loginDto));
    }
    @PostMapping("/customers/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerRegistrationDto customerRegistrationDto) {

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(
                        authService.register(customerRegistrationDto)
                );
    }
}
