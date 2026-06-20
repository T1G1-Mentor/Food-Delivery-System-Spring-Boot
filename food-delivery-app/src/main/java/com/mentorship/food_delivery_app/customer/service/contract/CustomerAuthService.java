package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;

public interface CustomerAuthService {

    String register(CustomerRegistrationDto customerRegistrationDto);
}
