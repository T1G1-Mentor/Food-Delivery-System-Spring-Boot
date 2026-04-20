package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}
