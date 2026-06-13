package com.mentorship.food_delivery_app.common.service.contract;

import com.mentorship.food_delivery_app.common.dto.EmailEventRecord;

public interface EmailService {
    void sendEmailAsync(EmailEventRecord emailEventRecord);
}
