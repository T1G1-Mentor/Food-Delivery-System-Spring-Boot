package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentProcessHandler extends OrderHandler {
    private final PaymentService paymentService;

    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {
//        TODO: Create payment strategy & handle the payment to proceed with order status payment failed if payment was not successful
        paymentService.processPayment();// dummy payment

        return this.handleNext(context);
    }
}
