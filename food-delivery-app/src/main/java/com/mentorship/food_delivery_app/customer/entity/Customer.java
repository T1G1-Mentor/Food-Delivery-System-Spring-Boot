package com.mentorship.food_delivery_app.customer.entity;

import com.mentorship.food_delivery_app.payment.entity.PaymentTypeConfig;
import com.mentorship.food_delivery_app.payment.entity.enums.PaymentIntegrationType;
import com.mentorship.food_delivery_app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
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

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_preferred_payment_type", length = 20)
    private PaymentIntegrationType preferredPayment;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<CustomerAddress> addresses;

    public String getFullName() {
        return this.user.getFullName();
    }
}
