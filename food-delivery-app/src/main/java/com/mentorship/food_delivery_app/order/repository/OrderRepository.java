package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {

    @Query("""
                    SELECT o FROM Order o
                    JOIN FETCH o.trackingHistory th
                    JOIN FETCH o.customer c
                    JOIN FETCH c.user
                    WHERE o.orderId= :orderId AND o.branch.admin.id= :userId
            """)
    Optional<Order> fetchOrderWithTrackingAndRestaurantBranchAndCustomer(UUID orderId, UUID userId);

}
