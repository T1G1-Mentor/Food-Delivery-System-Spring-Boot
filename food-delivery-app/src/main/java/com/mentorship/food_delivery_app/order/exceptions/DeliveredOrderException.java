package com.mentorship.food_delivery_app.order.exceptions;

public class DeliveredOrderException extends RuntimeException {
    public DeliveredOrderException(String message) {
        super(message);
    }
}
