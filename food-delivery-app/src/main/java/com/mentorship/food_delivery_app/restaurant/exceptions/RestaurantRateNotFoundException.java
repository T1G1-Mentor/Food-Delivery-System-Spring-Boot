package com.mentorship.food_delivery_app.restaurant.exceptions;

public class RestaurantRateNotFoundException extends RuntimeException {
    public RestaurantRateNotFoundException(String message) {
        super(message);
    }
}
