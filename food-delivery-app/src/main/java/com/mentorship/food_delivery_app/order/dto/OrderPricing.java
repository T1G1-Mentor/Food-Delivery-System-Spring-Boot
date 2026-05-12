package com.mentorship.food_delivery_app.order.dto;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.common.exceptions.ResourceUnavailableException;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;

import java.math.BigDecimal;

public record OrderPricing (
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal discount,
        BigDecimal total
){

    public static OrderPricing calculate(Cart cart, Coupon coupon) {
        BigDecimal subtotal = cart.calculateTotal();
        BigDecimal fee = cart.getCurrentRestaurant().getDeliveryFee();
        BigDecimal discount = resolveDiscount(coupon);
        BigDecimal total = subtotal.add(fee).subtract(discount);
        return new OrderPricing(subtotal, fee, discount, total);
    }

    private static BigDecimal resolveDiscount(Coupon coupon) {
        if (coupon == null) return BigDecimal.ZERO;

        if(!coupon.isValid()) {
            throw new ResourceUnavailableException("Coupon is not valid");
        }

        return coupon.getAmount();
    }
}