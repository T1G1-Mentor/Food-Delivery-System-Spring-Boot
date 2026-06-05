package com.mentorship.food_delivery_app.customer.entity;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.request.ModifyCustomerAddressRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Entity
@Table(name = "customer_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_address_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_address_customer_id", nullable = false)
    private Customer customer;

    @Column(name = "customer_address_label", nullable = false, length = 20)
    private String label;

    @Column(name = "customer_address_city", nullable = false, length = 20)
    private String city;

    @Column(name = "customer_address_street", nullable = false, length = 20)
    private String street;

    @Column(name = "customer_address_building", nullable = false, length = 20)
    private String building;

    @Column(name = "customer_address_apartment", nullable = false, length = 20)
    private String apartment;

    @Column(name = "customer_address_phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "customer_address_note", length = 500)
    private String note;

    public void applyModifications(ModifyCustomerAddressRequestDto addressRequestDto) {
        if (addressRequestDto.label() != null
                && !addressRequestDto.label().isEmpty()
                && !addressRequestDto.label().equals(this.label))
            this.label = addressRequestDto.label();

        if (addressRequestDto.city() != null
                && !addressRequestDto.city().isEmpty()
                && !addressRequestDto.city().equals(this.city))
            this.city = addressRequestDto.city();

        if (addressRequestDto.street() != null
                && !addressRequestDto.street().isEmpty()
                && !addressRequestDto.street().equals(this.street)
        )
            this.street = addressRequestDto.street();

        if (addressRequestDto.building() != null
                && !addressRequestDto.building().isEmpty()
                && !addressRequestDto.building().equals(this.building)
        )
            this.building = addressRequestDto.building();

        if (addressRequestDto.apartment() != null
                && !addressRequestDto.apartment().isEmpty()
                && !addressRequestDto.apartment().equals(this.apartment)
        )
            this.apartment = addressRequestDto.apartment();

        if (addressRequestDto.addressPhoneNumber() != null
                && !addressRequestDto.addressPhoneNumber().isEmpty()
                && !addressRequestDto.addressPhoneNumber().equals(this.phoneNumber)
        )
            this.phoneNumber = addressRequestDto.addressPhoneNumber();

        if (addressRequestDto.addressNote() != null
                && !addressRequestDto.addressNote().isEmpty()
                && !addressRequestDto.addressNote().equals(this.note)
        )
            this.note = addressRequestDto.addressNote();
    }
}
