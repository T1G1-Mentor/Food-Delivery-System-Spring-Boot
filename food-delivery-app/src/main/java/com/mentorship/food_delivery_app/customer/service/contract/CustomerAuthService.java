package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.customer.dto.request.CustomerRegistrationDto;

public interface CustomerAuthService {

    String register(CustomerRegistrationDto customerRegistrationDto);
}
