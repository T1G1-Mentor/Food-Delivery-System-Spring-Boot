package com.mentorship.food_delivery_app.customer.repository;

import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CustomerAddressRepository extends CrudRepository<CustomerAddress, UUID> {
}
