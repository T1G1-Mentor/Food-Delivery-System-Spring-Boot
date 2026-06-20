package com.mentorship.food_delivery_app.auth.service.contract;

import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.auth.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(CustomerRegistrationDto customerRegistrationDto);
}
