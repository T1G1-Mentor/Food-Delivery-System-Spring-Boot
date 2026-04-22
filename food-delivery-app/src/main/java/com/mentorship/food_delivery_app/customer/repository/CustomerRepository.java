package com.mentorship.food_delivery_app.customer.repository;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("""
            SELECT c FROM Customer c
            LEFT JOIN FETCH c.cart cr
            LEFT JOIN FETCH cr.cartItems ci
            LEFT JOIN FETCH ci.menuItem
            WHERE c.user.id = :userId
            """)
    Optional<Customer> fetchCustomerWithCartInfoByUserId(UUID userId);

    @Query("""
            SELECT c FROM Customer c
            LEFT JOIN FETCH c.cart cr
            WHERE c.user.id = :userId
            """)
    Optional<Customer> fetchCustomerWithCartOnlyByUserId(UUID userId);
}
