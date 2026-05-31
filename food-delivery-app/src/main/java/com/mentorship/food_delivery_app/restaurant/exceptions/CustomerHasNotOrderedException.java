package com.mentorship.food_delivery_app.restaurant.exceptions;

public class CustomerHasNotOrderedException extends RuntimeException {
    public CustomerHasNotOrderedException(String message) {
        super(message);
    }
}
