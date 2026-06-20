package com.mentorship.food_delivery_app.user.controller;

import com.mentorship.food_delivery_app.user.dto.request.AdminCreationDto;
import com.mentorship.food_delivery_app.user.service.contract.AdminManagementService;
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
@RequestMapping("/api/v1/admin/management")
@RequiredArgsConstructor
@Tag(name = "Admin Management Controller", description = "For Admins to manage new Admin")
public class AdminManagementController {
    private final AdminManagementService adminManagementService;

    @PostMapping()
    public ResponseEntity<String> createAdmin(@RequestBody @Valid AdminCreationDto adminCreationDto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminManagementService.createAdmin(adminCreationDto));
    }
}
