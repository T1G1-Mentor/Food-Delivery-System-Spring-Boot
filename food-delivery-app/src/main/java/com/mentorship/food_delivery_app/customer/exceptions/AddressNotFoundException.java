package com.mentorship.food_delivery_app.customer.exceptions;

public class AddressNotFoundException extends RuntimeException{
    public AddressNotFoundException(String message) {
        super(message);
    }
}
