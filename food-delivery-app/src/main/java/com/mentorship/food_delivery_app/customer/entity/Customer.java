package com.mentorship.food_delivery_app.customer.entity;

import com.mentorship.food_delivery_app.payment.entity.PaymentTypeConfig;
import com.mentorship.food_delivery_app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_default_address_id")
    private CustomerAddress defaultAddress;

    @ManyToOne()
    @JoinColumn(name = "customer_preferred_payment_id")
    private PaymentTypeConfig preferredPaymentType;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<CustomerAddress> addresses;
}
