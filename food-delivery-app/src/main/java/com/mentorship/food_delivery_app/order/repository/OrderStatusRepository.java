package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.OrderStatus;
import org.springframework.data.repository.CrudRepository;

public interface OrderStatusRepository extends CrudRepository<OrderStatus, Integer> {
}
