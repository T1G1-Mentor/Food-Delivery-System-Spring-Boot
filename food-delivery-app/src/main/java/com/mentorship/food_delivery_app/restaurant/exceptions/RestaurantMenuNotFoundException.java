package com.mentorship.food_delivery_app.restaurant.exceptions;

public class RestaurantMenuNotFoundException extends RuntimeException {
    public RestaurantMenuNotFoundException(String message) {
        super(message);
    }
}
