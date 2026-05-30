package com.mentorship.food_delivery_app.payment.entity;

import com.mentorship.food_delivery_app.payment.entity.enums.PaymentIntegrationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_type_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTypeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_type_config_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_integration_type", length = 20, nullable = false)
    private PaymentIntegrationType paymentIntegrationType;

    @Column(name = "config_details", columnDefinition = "TEXT", nullable = false)
    private String configDetails;
}
