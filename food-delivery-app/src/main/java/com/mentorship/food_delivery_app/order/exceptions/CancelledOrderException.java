package com.mentorship.food_delivery_app.order.exceptions;

public class CancelledOrderException extends RuntimeException {
    public CancelledOrderException(String message) {
        super(message);
    }
}
