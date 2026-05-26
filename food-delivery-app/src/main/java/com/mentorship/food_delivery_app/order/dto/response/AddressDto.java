package com.mentorship.food_delivery_app.order.dto.response;

public record AddressDto(
        String city,
        String street,
        String building,
        String apartment
) {
}
