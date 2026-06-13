package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;

public abstract class OrderHandler {
    private OrderHandler next;

    public static OrderHandler processOrder(OrderHandler first, OrderHandler... chain) {

        OrderHandler head = first;
        for (OrderHandler nextHandler : chain) {
            head.next = nextHandler;
            head = head.next;
        }
        return first;
    }

    public abstract OrderResponseDto handle(OrderProcessingContext context);

    protected OrderResponseDto handleNext(OrderProcessingContext context) {
        if (next == null)
            return context.getResponseDto();

        return next.handle(context);
    }

}
