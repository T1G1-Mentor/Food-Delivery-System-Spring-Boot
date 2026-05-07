package com.mentorship.food_delivery_app.order.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum OrderStatus {

    PENDING("Pending"),
    @JsonAlias({"in progress", "in-progress"})
    IN_PROGRESS("In Progress"),
    @JsonAlias({"on the way", "on-the-way"})
    ON_THE_WAY("On The Way"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    final String exposableName;

    OrderStatus(String exposableName) {
        this.exposableName = exposableName;
    }

    public String getExposableName() {
        return exposableName;
    }
}
