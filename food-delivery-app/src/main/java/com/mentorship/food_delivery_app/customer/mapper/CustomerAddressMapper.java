package com.mentorship.food_delivery_app.customer.mapper;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import org.springframework.stereotype.Component;

@Component
public class CustomerAddressMapper {
    public CustomerAddressResponseDto toResponse(CustomerAddress customerAddress) {
        return new CustomerAddressResponseDto(customerAddress.getId(),
                customerAddress.getLabel(),
                customerAddress.getCity(),
                customerAddress.getStreet(),
                customerAddress.getBuilding(),
                customerAddress.getApartment(),
                customerAddress.getPhoneNumber(),
                customerAddress.getNote());
    }
}
