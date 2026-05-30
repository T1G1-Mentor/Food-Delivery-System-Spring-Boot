package com.mentorship.food_delivery_app.customer.dto.customeraddress.response;

import java.util.UUID;

public record CustomerAddressResponseDto(
        UUID addressId,
        String label,
        String city,
        String street,
        String building,
        String apartment,
        String addressPhoneNumber,
        String addressNote,
        boolean isDefault
) {
}
