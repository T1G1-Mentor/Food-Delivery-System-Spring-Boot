package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    @Query("""
                SELECT mi FROM MenuItem mi
                JOIN FETCH mi.menu m
                JOIN FETCH m.restaurantBranch
                WHERE mi.menuItemId = :menuItemId
            """)
    Optional<MenuItem> findById(UUID menuItemId);

    @Query("""
                SELECT mi FROM MenuItem mi
                WHERE mi.menuItemId IN :menuItemIds
            """)
    List<MenuItem> findMenuItemsByIds(List<UUID> menuItemIds);

    @Query("""
                SELECT mi FROM MenuItem mi
                WHERE mi.menuItemId = :menuItemId AND mi.menu.restaurantMenuId = :restaurantMenuId
            """)
    Optional<MenuItem> findByIdAndMenuId(UUID menuItemId, UUID restaurantMenuId);

    @Query("""
    SELECT new com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto(
    mi.menuItemId,
    mi.name,
    mi.description,
    mi.price
    ) FROM MenuItem mi
    WHERE mi.menu.restaurantMenuId = :restaurantMenuId
""")
    List<MenuItemDto> findAllByMenuId(UUID restaurantMenuId);
}
