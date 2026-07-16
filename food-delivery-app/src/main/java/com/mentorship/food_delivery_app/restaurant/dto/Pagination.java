package com.mentorship.food_delivery_app.restaurant.dto;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;

import java.util.List;
import java.util.UUID;

public record Pagination(
        boolean hasNext,
        UUID nextCursor,
        List<SearchMenuItemResponse> responses
) {
}
