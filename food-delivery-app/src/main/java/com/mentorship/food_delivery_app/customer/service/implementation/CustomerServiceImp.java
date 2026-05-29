package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.CustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import com.mentorship.food_delivery_app.customer.exceptions.CustomerNotFoundException;
import com.mentorship.food_delivery_app.customer.mapper.CustomerAddressMapper;
import com.mentorship.food_delivery_app.customer.repository.CustomerRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerAddressService;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final CustomerAddressService customerAddressService;
    private final CustomerAddressMapper addressMapper;
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

    @Transactional
    @Override
    public UUID createCustomerAddress(CustomerAddressRequestDto addressRequestDto) {
        Customer customer = this.getLoggedinCustomer();

        return customerAddressService.createCustomerAddress(addressRequestDto, customer);
    }

    @Transactional
    @Override
    public void updateCustomerAddress(UUID addressId, UUID customerId, ModifyCustomerAddressRequestDto addressRequestDto) {
        this.customerAddressService.updateCustomerAddress(
                addressId, customerId, addressRequestDto);
    }

    @Transactional
    @Override
    public void deleteCustomerAddress(UUID addressId) {
        Customer customer = this.getLoggedinCustomer();
        CustomerAddress customerAddress = customer.getDefaultAddress();

        if (customerAddress != null && customerAddress.getId().equals(addressId))
            customerRepository.updateCustomerDefaultAddress(customer.getId(), addressId);

        this.customerAddressService.deleteCustomerAddress(addressId, customer.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerAddressResponseDto getCustomerAddress(UUID addressId, UUID customerId) {

        return addressMapper.toResponse
                (this.customerAddressService.getCustomerAddress(addressId, customerId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerAddressResponseDto> getAllCustomerAddresses(UUID customerId) {
        return this.customerAddressService.getAllCustomerAddresses(customerId)
                .stream().
                map(addressMapper::toResponse)
                .toList();
    }
}
