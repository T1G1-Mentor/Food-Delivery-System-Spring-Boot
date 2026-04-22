package com.mentorship.food_delivery_app.common.enums;

public enum ErrorMessage {
    CUSTOMER_NOT_FOUND("Customer not found"),
    CART_NOT_FOUND_TO_REMOVE_FROM("Cart not found, Please create cart before removing items from."),
    CART_ITEM_NOT_FOUND("This item does not exist in your cart."),
    CART_NOT_FOUND("Cart does not exists, Please create your cart first."),
    ITEM_NOT_FOUND("Menu item not found."),
    ITEM_DIFFERENT_RESTAURANT("This item belongs to a different restaurant. Please clear your cart first.");

    private final String message;

     ErrorMessage(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
