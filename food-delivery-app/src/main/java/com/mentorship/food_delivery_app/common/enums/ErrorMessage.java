package com.mentorship.food_delivery_app.common.enums;

public enum ErrorMessage {
    CUSTOMER_NOT_FOUND("Customer not found"),
    CART_ITEM_NOT_FOUND("This item does not exist in your cart."),
    CART_NOT_FOUND("Cart does not exists, Please create your cart first."),
    ITEM_NOT_FOUND("Menu item not found."),
    COUPON_NOT_FOUND("Coupon not found."),
    ITEM_DIFFERENT_RESTAURANT("This item belongs to a different restaurant. Please clear your cart first."),
    MENU_ITEM_NOT_AVAILABLE("This menu item not available at the moment. Please try again later."),
    ORDER_NOT_FOUND("The order you are looking for does not exist."),
    ORDER_ALREADY_DELIVERED("The order has already been delivered"),
    ORDER_ALREADY_CANCELLED("The order has already been cancelled"),
    RESTAURANT_CLOSED("The restaurant is currently closed. Please try again later.");

    private final String message;

     ErrorMessage(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
