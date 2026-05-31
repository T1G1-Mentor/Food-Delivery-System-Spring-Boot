package com.mentorship.food_delivery_app.restaurant.exceptions;

public class CustomerAlreadyRatedException extends RuntimeException {
    public CustomerAlreadyRatedException(String message) {
        super(message);
    }
}
