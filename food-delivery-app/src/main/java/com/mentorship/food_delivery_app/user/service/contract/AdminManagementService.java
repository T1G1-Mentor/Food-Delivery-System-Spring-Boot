package com.mentorship.food_delivery_app.user.service.contract;

import com.mentorship.food_delivery_app.user.dto.request.AdminCreationDto;

public interface AdminManagementService {
    String createAdmin(AdminCreationDto adminCreationDto);
}
