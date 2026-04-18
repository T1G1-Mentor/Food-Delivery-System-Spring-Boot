package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderTrackingRepository extends CrudRepository<OrderTracking, UUID> {
}
