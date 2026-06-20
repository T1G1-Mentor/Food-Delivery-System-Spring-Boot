package com.mentorship.food_delivery_app.customer.controller;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.CustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/addresses")
@RequiredArgsConstructor
@Tag(name = "Customer Address", description = "For customers to manage their addresses")
public class CustomerAddressController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerAddressResponseDto>> getAllAddresses() {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        return ResponseEntity.ok(customerService.getAllCustomerAddresses(customer.getCustomerId()));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<CustomerAddressResponseDto> getAddress(@PathVariable UUID addressId) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        return ResponseEntity.ok(customerService.getCustomerAddress(addressId,
                customer.getCustomerId()));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable UUID addressId) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        customerService.deleteCustomerAddress(addressId, customer.getCustomerId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<Void> createCustomerAddress(@RequestBody @Valid CustomerAddressRequestDto addressRequestDto) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        UUID addressId = customerService.createCustomerAddress(addressRequestDto, customer.getCustomerId());

        return ResponseEntity.
                created(URI.create("/api/v1/customers/addresses/" + addressId)).build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Void> updateCustomerAddress(@RequestBody @Valid ModifyCustomerAddressRequestDto addressRequestDto,
                                                      @PathVariable UUID addressId) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        customerService.updateCustomerAddress(addressId, customer.getCustomerId(), addressRequestDto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> setCustomerDefaultAddress(@PathVariable UUID addressId) {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        customerService.setCustomerDefaultAddress(addressId, customer.getCustomerId());
        return ResponseEntity.
                noContent().build();
    }

    @GetMapping("/default")
    public ResponseEntity<CustomerAddressResponseDto> getCustomerDefaultAddress() {
        Customer customer = customerService.getLoggedinCustomer(); // will be replaced with authorization principal to get the id

        return ResponseEntity.ok(customerService.getCustomerDefaultAddress(customer.getCustomerId()));
    }
}
