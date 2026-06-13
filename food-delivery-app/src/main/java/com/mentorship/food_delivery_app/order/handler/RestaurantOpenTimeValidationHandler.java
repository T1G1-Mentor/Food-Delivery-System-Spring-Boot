package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantBranchClosedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantOpenTimeValidationHandler extends OrderHandler {


    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {
        RestaurantBranch restaurantBranch = context.getRestaurantBranch();

        if (!restaurantBranch.isOpen())
            throw new RestaurantBranchClosedException(ErrorMessage.RESTAURANT_BRANCH_CLOSED.getMessage());


        return this.handleNext(context);
    }
}
