package com.mentorship.food_delivery_app.customer.repository;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.payment.entity.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("""
            SELECT c FROM Customer c
            WHERE c.user.userId = :userId
            """)
    Optional<Customer> findByUserId(UUID userId);


    /**
     * Updating customer default address before deleting the address
     * saving it from violating {@code FK constraint}
     * This query searches for an address that is not the one that will be deleted
     * if there isn't any other addresses exists in the DB it will be {@code NULL}
     *
     */
    @Modifying
    @Query(value = """
            UPDATE customer c SET customer_default_address_id =
                        (SELECT ca.customer_address_id FROM customer_address ca
                                WHERE ca.customer_address_customer_id = c.customer_id AND ca.customer_address_id != :addressId
                                        ORDER BY ca.customer_address_id ASC LIMIT 1)
             WHERE c.customer_id = :customerId""",
            nativeQuery = true)
    void updateCustomerDefaultAddress(UUID customerId, UUID addressId);

    @Query("""
                SELECT c FROM Customer c
                LEFT JOIN FETCH c.defaultAddress
                WHERE c.customerId = :customerId
            """)
    Optional<Customer> findByIdWithDefaultAddress(UUID customerId);

    @Query("""
    SELECT c.preferredPayment FROM Customer c
    WHERE c.customerId = :customerId
""")
    Optional<PaymentMethod> getPreferredPaymentById(UUID customerId);

    @Query("""
                SELECT c.id FROM Customer c
                WHERE c.user.userId = :userId
            """)
    Optional<UUID> findIdByUserId(UUID userId);
}
