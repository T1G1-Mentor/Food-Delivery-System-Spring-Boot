package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.exceptions.RestaurantMismatchException;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartValidationHandler extends OrderHandler {


    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {
        Cart cart = context.getCart();

        if (!cart.getCurrentRestaurant().getRestaurantBranchId()
                .equals(context.getRequestRestaurantBranchId()))
            throw new RestaurantMismatchException(ErrorMessage.RESTAURANT_MISMATCH.getMessage());

        return this.handleNext(context);
    }
}
