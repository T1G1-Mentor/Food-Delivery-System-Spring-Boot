package com.mentorship.food_delivery_app.payment.entity;

import com.mentorship.food_delivery_app.payment.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_provider_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProviderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_type_config_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider_name", length = 20, nullable = false)
    private PaymentMethod paymentProvider;

    @Column(name = "config_details", columnDefinition = "TEXT", nullable = false)
    private String configDetails;
}
