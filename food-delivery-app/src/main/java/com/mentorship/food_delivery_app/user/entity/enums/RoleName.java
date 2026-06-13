package com.mentorship.food_delivery_app.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum RoleName {
    ROLE_CUSTOMER,
    @JsonAlias({"Admin"})
    ROLE_ADMIN
}
