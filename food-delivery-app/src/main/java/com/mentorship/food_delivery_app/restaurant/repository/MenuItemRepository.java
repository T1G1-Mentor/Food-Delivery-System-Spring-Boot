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
            SELECT
                        mi.menuItemId,
                        mi.menuItemName,
                        mi.menuItemDescription,
                        mi.menuItemPrice,
                        m.restaurant_menu_id AS restaurantMenuId,
                        m.restaurant_menu_name AS restaurantMenuName,
                        b.branch_id AS branchId,
                        r.restaurant_name AS restaurantName
                    FROM (SELECT
                        mi.menu_item_id AS menuItemId,
                        mi.menu_item_name AS menuItemName,
                        mi.menu_item_description AS menuItemDescription,
                        mi.menu_item_price AS menuItemPrice,
                        mi.restaurant_menu_id
                          FROM menu_item mi
                          WHERE mi.is_deleted = false
                          AND mi.is_available = true
                          AND mi.search_vector @@ to_tsquery('english', :query || ':*')
                          ) mi
                    JOIN restaurant_menu m ON mi.restaurant_menu_id = m.restaurant_menu_id
                    JOIN restaurant_branch b ON m.restaurant_menu_rest_branch_id = b.branch_id
                    JOIN restaurant r ON b.branch_rest_id = r.restaurant_id;
            """, nativeQuery = true)
    List<SearchMenuItemResponse> searchMenuItem(String query);
}
