package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.repository.CustomerRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer fetchCustomerWithCartInfoByUserId(UUID userId) {
        return customerRepository.fetchCustomerWithCartInfoByUserId(userId)
                .orElseThrow(
                        ()->new ResourceNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND.getMessage()
                        ));
    }

    @Override
    public Customer fetchCustomerWithCartOnlyByUserId(UUID userId) {
        return customerRepository.fetchCustomerWithCartOnlyByUserId(userId)
                .orElseThrow(
                        ()->new ResourceNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND.getMessage()
                        ));
    }
}
