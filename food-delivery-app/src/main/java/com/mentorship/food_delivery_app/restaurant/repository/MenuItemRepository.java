package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
}
