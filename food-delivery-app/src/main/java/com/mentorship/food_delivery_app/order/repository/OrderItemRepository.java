package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends CrudRepository<OrderItem, UUID> {

    @Query("""
                SELECT oi FROM OrderItem oi
                WHERE oi.order.orderId = :orderId
            """)
    List<OrderItem> findByOrderId(UUID orderId);
}
