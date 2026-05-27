package com.mentorship.food_delivery_app.customer.service.contract;

import com.mentorship.food_delivery_app.customer.entity.Customer;

import java.util.UUID;

public interface CustomerService {

    /**
     * @return Logged in customer {@code Custoemr}
     * */
    Customer getLoggedinCustomer( );

    /**
     * Performs a soft delete operation on the customer's user account*/
    void deactivateAccount();

}
