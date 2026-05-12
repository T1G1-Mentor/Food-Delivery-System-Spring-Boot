package com.mentorship.food_delivery_app.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeliveryAddressDto (
        @NotBlank @Size(max = 20)
        String city,

        @NotBlank @Size(max = 20)
        String street,

        @NotBlank @Size(max = 20)
        String building,

        @NotBlank @Size(max = 20)
        String apartment,

        @NotBlank @Size(max = 15)
        String phoneNumber,

        @Size(max = 500)
        String note
) {

}
