package com.mentorship.food_delivery_app.order.entity;

import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAddress {
    private String city;
    private String street;
    private String building;
    private String apartment;
    private String phoneNumber;
    private String note;

    // Built from CustomerAddress at order time
    public static DeliveryAddress from(CustomerAddress source) {
        return DeliveryAddress.builder()
                .city(source.getCity())
                .street(source.getStreet())
                .building(source.getBuilding())
                .apartment(source.getApartment())
                .phoneNumber(source.getPhoneNumber())
                .note(source.getNote())
                .build();
    }

    public static DeliveryAddress from(DeliveryAddressDto dto) {
        return DeliveryAddress.builder()
                .city(dto.city())
                .street(dto.street())
                .building(dto.building())
                .apartment(dto.apartment())
                .phoneNumber(dto.phoneNumber())
                .note(dto.note())
                .build();
    }
}
