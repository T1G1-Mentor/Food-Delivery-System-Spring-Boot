package com.mentorship.food_delivery_app.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Will be changed to enum once we agree on the statuses of our system transactions
 */
@Entity
@Table(name = "transaction_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatus {

    @Id
    @Column(name = "status", length = 20, nullable = false)
    private String status;
}
