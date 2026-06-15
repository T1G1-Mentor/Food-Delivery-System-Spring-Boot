package com.mentorship.food_delivery_app.restaurant.dto.menuitem.response;

import java.math.BigDecimal;
import java.util.UUID;

public interface SearchMenuItemResponse {
    UUID getMenuItemId();
    String getMenuItemName();
    String getMenuItemDescription();
    BigDecimal getMenuItemPrice();
    UUID getRestaurantMenuId();
    String getRestaurantMenuName();
    UUID getBranchId();
    String getRestaurantName();
}
