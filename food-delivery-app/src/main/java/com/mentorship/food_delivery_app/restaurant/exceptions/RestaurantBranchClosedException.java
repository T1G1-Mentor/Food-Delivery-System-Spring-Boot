package com.mentorship.food_delivery_app.restaurant.exceptions;

public class RestaurantBranchClosedException extends RuntimeException {
    public RestaurantBranchClosedException(String message) {
        super(message);
    }
}
