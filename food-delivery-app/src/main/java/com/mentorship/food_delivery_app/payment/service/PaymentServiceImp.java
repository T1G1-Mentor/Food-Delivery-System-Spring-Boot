package com.mentorship.food_delivery_app.payment.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImp implements PaymentService{
    @Override
    public boolean processPayment() {
        return true;
    }
}
