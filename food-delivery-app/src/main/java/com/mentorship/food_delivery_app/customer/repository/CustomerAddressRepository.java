package com.mentorship.food_delivery_app.customer.repository;

import com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerAddressRepository extends CrudRepository<CustomerAddress, UUID> {

    @Query("""
                SELECT ca FROM CustomerAddress ca
                WHERE ca.customer.id = :customerId
            """)
    List<CustomerAddress> findAllByCustomerId(UUID customerId);

    @Modifying()
    @Query("""
                DELETE FROM CustomerAddress ca
                WHERE ca.id= :addressId AND ca.customer.id= :customerId
            """)
    void deleteByIdAndCustomerId(UUID addressId, UUID customerId);

    @Query("""
                SELECT ca FROM CustomerAddress ca
                WHERE ca.id = :addressId AND ca.customer.id = :customerId
            """)
    Optional<CustomerAddress> findByIdAndCustomerId(UUID addressId, UUID customerId);

    @Query(
            """
                    SELECT new com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto(
                        ca.customerAddressId,
                        ca.label,
                        ca.city,
                        ca.street,
                        ca.building,
                        ca.apartment,
                        ca.phoneNumber,
                        ca.note,
                                (CASE WHEN c.defaultAddress.customerAddressId = ca.customerAddressId THEN true ELSE false END)
                                )
                        FROM CustomerAddress ca
                        JOIN ca.customer c
                        WHERE ca.customerAddressId = :addressId AND c.customerId = :customerId
                    """)
    Optional<CustomerAddressResponseDto> findDtoByIdAndCustomerId(UUID addressId, UUID customerId);

    @Query("""
            SELECT new com.mentorship.food_delivery_app.customer.dto.customeraddress.response.CustomerAddressResponseDto(
                               ca.id,
                               ca.label,
                               ca.city,
                               ca.street,
                               ca.building,
                               ca.apartment,
                               ca.phoneNumber,
                               ca.note,
                               (CASE WHEN c.defaultAddress.id = ca.id THEN true ELSE false END)
                           )
                           FROM CustomerAddress ca
                           JOIN ca.customer c
                           WHERE c.customerId = :customerId
            """)
    List<CustomerAddressResponseDto> findAllDtoByCustomerId(UUID customerId);
}
