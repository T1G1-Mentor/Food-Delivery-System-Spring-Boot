package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.common.service.contract.EmailService;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.entity.Order;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinalizeOrderHandler extends OrderHandler {
    private final EmailService emailService;


    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {

        Order savedOrder = context.createAndPersistOrder();

//        notifyOrderPlaced(savedOrder);

        return this.handleNext(context);
    }


    private void notifyOrderPlaced(Order order) {

        emailService.sendEmailAsync(
                order.getCustomerEmail(),
                "Order Confirmation",
                "Your order has been placed. Order ID: " + order.getOrderId()
        );

    }


}
