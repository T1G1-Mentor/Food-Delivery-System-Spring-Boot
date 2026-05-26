package com.mentorship.food_delivery_app.user.service.contract;

import com.mentorship.food_delivery_app.user.entity.User;

import java.util.UUID;

public interface UserService {

    User getDummyLoggedInUser();

    void deactivateByUserId(UUID userId);
}
