package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.dto.response.OrderItemDto;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends CrudRepository<OrderItem, UUID> {

    @Query(value = """
            SELECT oi.order_item_id AS orderItemId
                   ,mi.menu_item_name AS menuItemName
                   ,oi.order_item_quantity AS quantity
                 ,oi.order_item_unit_price AS unitPrice
                 ,oi.order_item_subtotal AS subtotal
                 ,oi.order_item_note AS note FROM order_item oi JOIN menu_item mi ON oi.order_item_menu_item_id = mi.menu_item_id
            WHERE OI.order_item_order_id = :orderId
            """, nativeQuery = true)
    List<OrderItemDto> findByOrderId(UUID orderId);
}
