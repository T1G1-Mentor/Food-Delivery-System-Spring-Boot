package com.mentorship.food_delivery_app.customer.exceptions;

public class PreferredPaymentWasNotConfiguredException extends RuntimeException {
    public PreferredPaymentWasNotConfiguredException(String message) {
        super(message);
    }
}
