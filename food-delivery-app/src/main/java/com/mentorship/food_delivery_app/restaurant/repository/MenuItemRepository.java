package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    @Query("""
    SELECT mi FROM MenuItem mi
    JOIN FETCH menu m
    JOIN FETCH restaurantBranch
    WHERE mi.menuItemId = :menuItemId
""")
    Optional<MenuItem> findById(UUID menuItemId);

    @Query("""
    SELECT mi FROM MenuItem mi
    WHERE mi.menuItemId IN :menuItemIds
""")
    List<MenuItem> findMenuItemsByIds(List<UUID> menuItemIds);
}
