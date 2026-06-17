package com.mentorship.food_delivery_app.restaurant.exceptions;

public class DisabledRestaurantBranchException extends RuntimeException {
    public DisabledRestaurantBranchException(String message){
        super(message);
    }
}
