package com.mentorship.food_delivery_app.customer.controller;


import com.mentorship.food_delivery_app.customer.entity.Customer;
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
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id
//        the principal will also contain the user-Id
        customerService.deactivateAccount(customer.getId(), customer.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}
