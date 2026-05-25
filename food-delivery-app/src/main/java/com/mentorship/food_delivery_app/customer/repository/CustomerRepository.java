package com.mentorship.food_delivery_app.customer.repository;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("""
            SELECT c FROM Customer c
            WHERE c.user.id = :userId
            """)
    Optional<Customer> findByUserId(UUID userId);


}
