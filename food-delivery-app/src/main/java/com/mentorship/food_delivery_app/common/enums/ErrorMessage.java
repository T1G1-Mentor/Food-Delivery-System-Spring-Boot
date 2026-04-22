package com.mentorship.food_delivery_app.common.enums;

public enum ErrorMessage {
    CUSTOMER_NOT_FOUND("Customer not found"),
    CART_NOT_FOUND_TO_REMOVE_FROM("Cart not found, Please create cart before removing items from."),
    CART_ITEM_NOT_FOUND("This item does not exist in your cart.");

    private final String message;

     ErrorMessage(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
