package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.customer.entity.Customer;

import java.util.UUID;

public interface CustomerService {

    /**
     * Fetches customer with {@code userId}
     * @return Required {@code Custoemr} with his {@code Cart, CartItems, MenuItems}
     * @param userId The loggedIn userId.
     * */
    Customer fetchCustomerWithCartInfoByUserId(UUID userId);

    /**
     * Fetches customer with {@code userId}
     * @return Required {@code Custoemr} with his {@code Cart}
     * @param userId The loggedIn userId.
     * */
    Customer fetchCustomerWithCartOnlyByUserId(UUID userId);

}
