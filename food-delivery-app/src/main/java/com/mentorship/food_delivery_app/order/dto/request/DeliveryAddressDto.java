package com.mentorship.food_delivery_app.order.dto.request;

import com.mentorship.food_delivery_app.order.entity.DeliveryAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.DeleteMapping;

public record DeliveryAddressDto (
        @NotNull
        @NotBlank @Size(max = 20)
        String city,

        @NotNull
        @NotBlank @Size(max = 20)
        String street,

        @NotNull
        @NotBlank @Size(max = 20)
        String building,

        @NotNull
        @NotBlank @Size(max = 20)
        String apartment,

        @NotNull
        @NotBlank @Size(max = 15)
        String phoneNumber,

        @Size(max = 500)
        String note
) {
        public static DeliveryAddressDto from(DeliveryAddress address){
                return new DeliveryAddressDto(address.getCity(),
                         address.getStreet(),
                        address.getBuilding(),
                        address.getApartment(),
                        address.getPhoneNumber(),
                        address.getNote());
        }
}
