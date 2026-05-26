package com.mentorship.food_delivery_app.order.enums;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING("Pending","Your order is now Pending."),
    @JsonAlias({"in progress", "in-progress"})
    IN_PROGRESS("In Progress", "Your order is now In Progress."),
    @JsonAlias({"on the way", "on-the-way"})
    ON_THE_WAY("On The Way","We are On The Way"),
    DELIVERED("Delivered","Order has been Delivered."),
    CANCELLED("Cancelled","Order has been Cancelled.");

    final String exposableName;
    final String description;

}
