package com.mentorship.food_delivery_app.restaurant.repository;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import lombok.NonNull;
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
    @NonNull
    Optional<MenuItem> findById(@NonNull UUID menuItemId);

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

    @Query(value = """
            SELECT mi.menu_item_id, mi.menu_item_name, mi.menu_item_description, mi.menu_item_price,
                   m.restaurant_menu_id, m.restaurant_menu_name, b.branch_id, r.restaurant_name
            FROM (
                SELECT menu_item_id, menu_item_name, menu_item_description, menu_item_price, restaurant_menu_id
                FROM menu_item
                WHERE is_deleted = false
                  AND is_available = true
                  AND search_vector @@ to_tsquery('english', :query)
                  AND (:nextCursor is null or menu_item_id > :nextCursor)
                ORDER BY  menu_item_id
            
                LIMIT :pageSize
            ) mi
            JOIN restaurant_menu m ON m.restaurant_menu_id = mi.restaurant_menu_id
            JOIN restaurant_branch b ON b.branch_id = m.restaurant_menu_rest_branch_id
            JOIN restaurant r ON r.restaurant_id = b.branch_rest_id
            ORDER BY mi.menu_item_id;
            """, nativeQuery = true)
    List<SearchMenuItemResponse> searchMenuItem(String query, int pageSize, UUID nextCursor);
}
