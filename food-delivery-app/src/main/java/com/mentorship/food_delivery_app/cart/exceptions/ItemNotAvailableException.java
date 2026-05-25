package com.mentorship.food_delivery_app.cart.exceptions;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
