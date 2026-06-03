package com.mentorship.food_delivery_app.payment.entity;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.payment.entity.enums.PaymentMethod;
import com.mentorship.food_delivery_app.payment.entity.enums.PaymentProvider;
import com.mentorship.food_delivery_app.payment.entity.enums.TransactionStatus;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", length = 20, nullable = false)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_payment_provider", length = 20)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_rest_branch_id", nullable = false)
    private RestaurantBranch branch;

    @Positive(message = "Transaction amount must be greater than zero")
    @Column(name = "transaction_amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "transaction_time", updatable = false)
    private LocalDateTime transactionTime;
}
