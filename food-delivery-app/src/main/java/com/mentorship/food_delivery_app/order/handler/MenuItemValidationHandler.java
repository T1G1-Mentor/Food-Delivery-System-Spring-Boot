package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.cart.exceptions.ItemNotAvailableException;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MenuItemValidationHandler extends OrderHandler {

    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {

        List<MenuItem> menuItems = context.getMenuItems();

        validateCartItemsAvailability(menuItems, context.getRequestRestaurantBranchId());

        return this.handleNext(context);

    }

    private void validateCartItemsAvailability(List<MenuItem> menuItems, UUID restaurantBranchId) {
        List<String> unavailableItemNames = menuItems
                .stream()
                .filter(item -> !item.isAvailable() ||
                        !item.getMenu().getRestaurantBranch().getRestaurantBranchId().equals(restaurantBranchId))
                .map(item -> {
                    if (!item.getMenu().getRestaurantBranch().getRestaurantBranchId().equals(restaurantBranchId))
                        return item.getName() + " Does not belong to the specified restaurant.";
                    else
                        return item.getName() + " Is not available at the moment.";
                })
                .toList();

        if (!unavailableItemNames.isEmpty()) {
            throw new ItemNotAvailableException(unavailableItemNames.toString());
        }
    }
}
