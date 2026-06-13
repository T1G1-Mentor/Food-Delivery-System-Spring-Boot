package com.mentorship.food_delivery_app.common.service.contract;

import com.mentorship.food_delivery_app.common.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
