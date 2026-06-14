package com.mentorship.food_delivery_app.common.enums;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
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
    RESTAURANT_CLOSED("The restaurant is currently closed. Please try again later."),
    ADDRESS_NOT_FOUND("The address you are locking for does not exist."),
    NO_DEFAULT_ADDRESS_EXISTS("You do not have any addresses. Please create address first"),
    PREFERRED_PAYMENT_NOT_FOUND("Customer did not configure his preferred payment type"),
    RESTAURANT_NOT_FOUND("Restaurant not found."),
    RESTAURANT_RATE_NOT_FOUND("Rating not found."),
    CUSTOMER_HAS_NOT_ORDERED("You must have a delivered order from this restaurant before rating it."),
    CUSTOMER_ALREADY_RATED("You have already rated this restaurant."),
    RESTAURANT_MISMATCH("This restaurant does not match, please make sure that all items belongs to the same restaurant."),
    RESTAURANT_BRANCH_NOT_FOUND("This restaurant branch does not exist."),
    RESTAURANT_BRANCH_CLOSED("The restaurant is currently closed."),
    EMAIL_ALREADY_EXISTS("The email you provided already exists, please try another one."),
    ROLE_NOT_FOUND("The role you are looking for does not exist."),
    RESTAURANT_MENU_NOT_FOUND("The menu you are locking for does not exist."),
    RESTAURANT_MENU_DISABLED("This restaurant menu is currently disabled. Pleas try again later") ;
    private final String message;


}
