package com.mentorship.food_delivery_app.payment.repository;

import com.mentorship.food_delivery_app.payment.entity.PaymentProviderConfig;
import org.springframework.data.repository.CrudRepository;

public interface PaymentTypeConfigRepository extends CrudRepository<PaymentProviderConfig, Integer> {
}
