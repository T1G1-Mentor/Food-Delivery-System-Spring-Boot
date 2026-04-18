package com.mentorship.food_delivery_app.payment.repository;

import com.mentorship.food_delivery_app.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {
}
