package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.exceptions.CustomerNotFoundException;
import com.mentorship.food_delivery_app.customer.repository.CustomerRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;

    @Value("${app.test.user-id}")
    private String userId;

    @Override
    public Customer getLoggedinCustomer() {
        return customerRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(
                        () -> new CustomerNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND.getMessage()
                        ));
    }
}
