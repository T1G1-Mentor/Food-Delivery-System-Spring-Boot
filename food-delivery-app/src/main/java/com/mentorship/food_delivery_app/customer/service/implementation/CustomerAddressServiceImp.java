package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.CustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import com.mentorship.food_delivery_app.customer.exceptions.AddressNotFoundException;
import com.mentorship.food_delivery_app.customer.repository.CustomerAddressRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerAddressServiceImp implements CustomerAddressService {
    private final CustomerAddressRepository customerAddressRepository;


    @Transactional
    @Override
    public UUID createCustomerAddress(CustomerAddressRequestDto addressRequestDto, Customer customer) {
        log.info("Creating new address {}, for customer {}", addressRequestDto, customer.getCustomerId());

        CustomerAddress address = this.buildAddress(addressRequestDto, customer);

        CustomerAddress savedAddress = customerAddressRepository.save(address);

        if (customer.getDefaultAddress() == null)
            customer.setDefaultAddress(address);

        return savedAddress.getCustomerAddressId();
    }

    @Transactional
    @Override
    public void updateCustomerAddress(UUID addressId, UUID customerId, ModifyCustomerAddressRequestDto addressRequestDto) {
        log.info("Updating address {}, for customer {}.", addressId, customerId);

        CustomerAddress address = customerAddressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() ->
                        new AddressNotFoundException(ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

        address.applyModifications(addressRequestDto);
    }

    @Transactional
    @Override
    public void deleteCustomerAddress(UUID addressId, UUID customerId) {
        log.info("Deleting address {}, for customer {}", addressId, customerId);

        customerAddressRepository.deleteByIdAndCustomerId(addressId, customerId);
    }

    @Override
    public CustomerAddressResponseDto getCustomerAddressDto(UUID addressId, UUID customerId) {
        return customerAddressRepository.findDtoByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));
    }

    @Override
    public List<CustomerAddressResponseDto> getAllCustomerAddresses(UUID customerId) {
        return customerAddressRepository.findAllDtoByCustomerId(customerId);
    }

    @Override
    public CustomerAddress getCustomerAddress(UUID addressId, UUID customerId) {
        return customerAddressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() ->
                        new AddressNotFoundException((ErrorMessage.ADDRESS_NOT_FOUND.getMessage())));
    }


    private CustomerAddress buildAddress(CustomerAddressRequestDto addressRequestDto, Customer customer) {
        CustomerAddress address = CustomerAddress.builder()
                .customer(customer)
                .label(addressRequestDto.label())
                .city(addressRequestDto.city())
                .street(addressRequestDto.street())
                .building(addressRequestDto.building())
                .apartment(addressRequestDto.apartment())
                .phoneNumber(addressRequestDto.addressPhoneNumber())
                .build();
        if (addressRequestDto.addressNote() != null && !addressRequestDto.addressNote().isEmpty())
            address.setNote(addressRequestDto.addressNote());

        return address;
    }
}
