package com.mentorship.food_delivery_app.cart.exceptions;

public class RestaurantMismatchException extends RuntimeException{
    public RestaurantMismatchException(String message) {
        super(message);
    }
}
