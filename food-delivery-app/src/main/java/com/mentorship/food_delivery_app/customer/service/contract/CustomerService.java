package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.CustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    /**
     * @return Logged in customer {@code Custoemr}
     *
     */
    Customer getLoggedinCustomer();

    /**
     * Performs a soft delete operation on the customer's user account
     */
    void deactivateAccount(UUID customerId, UUID userId);

    UUID createCustomerAddress(CustomerAddressRequestDto addressRequestDto, UUID customerId);

    void updateCustomerAddress(UUID addressId, UUID customerId, ModifyCustomerAddressRequestDto addressRequestDto);

    void deleteCustomerAddress(UUID addressId, UUID customerId);

    CustomerAddressResponseDto getCustomerAddress(UUID addressId, UUID customerId);

    List<CustomerAddressResponseDto> getAllCustomerAddresses(UUID customerId);

    void setCustomerDefaultAddress(UUID addressId, UUID customerId);

    CustomerAddressResponseDto getCustomerDefaultAddress(UUID customerId);
}
