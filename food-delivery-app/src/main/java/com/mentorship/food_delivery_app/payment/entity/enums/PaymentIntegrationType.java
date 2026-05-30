package com.mentorship.food_delivery_app.payment.entity.enums;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentIntegrationType {
    @JsonAlias({"COD", "cash on delivery"})
    COD("Cash On Delivery");

    private final String exposableName;
}
