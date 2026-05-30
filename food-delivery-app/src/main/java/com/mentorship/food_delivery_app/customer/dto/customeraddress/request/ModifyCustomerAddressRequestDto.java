package com.mentorship.food_delivery_app.customer.dto.customeraddress.request;

import jakarta.validation.constraints.Size;

public record ModifyCustomerAddressRequestDto(

        @Size(max = 20, message = "Label exceeds the limit")
        String label,


        @Size(max = 20, message = "City exceeds the limit")
        String city,


        @Size(max = 20, message = "Street exceeds the limit")
        String street,

        @Size(max = 20, message = "Building exceeds the limit")
        String building,


        @Size(max = 20, message = "Apartment exceeds the limit")
        String apartment,


        @Size(max = 15, min = 11, message = "Address Phone number must be between 11 and 15 character")
        String addressPhoneNumber,

        @Size(max = 500, message = "Address note must be between 0 and 500 character")
        String addressNote
) {
}
