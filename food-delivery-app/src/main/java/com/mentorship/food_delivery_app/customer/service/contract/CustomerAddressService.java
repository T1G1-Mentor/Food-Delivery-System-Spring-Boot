package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.CustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;

import java.util.List;
import java.util.UUID;

public interface CustomerAddressService {

    UUID createCustomerAddress(CustomerAddressRequestDto addressRequestDto, Customer customer);

    void updateCustomerAddress(UUID addressId, UUID customerId, ModifyCustomerAddressRequestDto addressRequestDto);

    void deleteCustomerAddress(UUID addressId, UUID customerId);

    CustomerAddressResponseDto getCustomerAddressDto(UUID addressId, UUID customerId);

    List<CustomerAddressResponseDto> getAllCustomerAddresses(UUID customerId);

    CustomerAddress getCustomerAddress(UUID addressId, UUID customerId);
}
