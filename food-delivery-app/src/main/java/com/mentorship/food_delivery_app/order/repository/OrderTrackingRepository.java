package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderTrackingRepository extends CrudRepository<OrderTracking, UUID> {
    @Query("""
    SELECT new com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto(
    od.status,
    od.description,
    od.createdAt
    )
    FROM OrderTracking od
    WHERE od.order.orderId = :orderId AND od.order.customer.customerId = :customerId
    ORDER BY od.createdAt DESC
""")
    List<OrderTrackingDto> findAllByCustomerIdAndOrderId(UUID customerId, UUID orderId);

    @Query("""
    SELECT ot FROM OrderTracking ot
    WHERE ot.order.orderId = :orderId
""")
    List<OrderTracking> findByOrderId(UUID orderId);

}
