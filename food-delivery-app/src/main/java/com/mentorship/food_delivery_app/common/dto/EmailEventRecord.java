package com.mentorship.food_delivery_app.common.dto;

public record EmailEventRecord(
        String to,
        String subject,
        String body
) {
}
