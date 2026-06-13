package com.mentorship.food_delivery_app.restaurant.exceptions;

public class RestaurantBranchNotFoundException extends RuntimeException {
    public RestaurantBranchNotFoundException(String message) {
        super(message);
    }
}
