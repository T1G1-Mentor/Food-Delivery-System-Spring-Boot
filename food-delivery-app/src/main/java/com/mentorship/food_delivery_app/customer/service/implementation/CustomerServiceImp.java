package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.exceptions.CustomerNotFoundException;
import com.mentorship.food_delivery_app.customer.repository.CustomerRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Value("${app.test.user-id}")
    private String userId;

    @Override
    public Customer getLoggedinCustomer() {
        return customerRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(
                        () -> new CustomerNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND.getMessage()
                        ));
    }

    @Transactional
    @Override
    public void deactivateAccount() {
        Customer customer =
                this.getLoggedinCustomer();
        log.info("Deactivating account for customer with id {}", customer.getId());

        userService.deactivateByUserId(UUID.fromString(userId));
    }
}
