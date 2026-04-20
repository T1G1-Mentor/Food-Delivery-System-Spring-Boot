package com.mentorship.food_delivery_app.customer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
