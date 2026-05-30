package com.mentorship.food_delivery_app.customer.dto.request;

import com.mentorship.food_delivery_app.payment.entity.enums.PaymentIntegrationType;
import jakarta.validation.constraints.NotNull;

public record PreferredPaymentRequest(

        @NotNull(message = "Preferred payment cannot be null")
        PaymentIntegrationType preferredPayment
) {
}
