package com.mentorship.food_delivery_app.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Will be changed to enum once we agree on the integration types of our system
 */
@Entity
@Table(name = "payment_integration_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIntegrationType {

    @Id
    @Column(name = "payment_integration_type_name", length = 20)
    private String name;
}
