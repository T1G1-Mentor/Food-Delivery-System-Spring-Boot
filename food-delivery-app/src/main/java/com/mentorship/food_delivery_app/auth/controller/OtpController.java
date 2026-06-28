package com.mentorship.food_delivery_app.auth.controller;

import com.mentorship.food_delivery_app.auth.dto.OtpSendRequest;
import com.mentorship.food_delivery_app.auth.dto.OtpVerifyRequest;
import com.mentorship.food_delivery_app.auth.service.contract.OtpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth/otp")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "For customer registration, and both Admin, and Customer login")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody OtpSendRequest request) {
        otpService.sendOtp(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@Valid @RequestBody OtpVerifyRequest request) {
        otpService.verifyOtp(request.email(), request.code());
        return ResponseEntity.ok().build();
    }
}
