package com.mentorship.food_delivery_app.user.service.contract;

import com.mentorship.food_delivery_app.user.entity.Role;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    User getDummyLoggedInUser();

    void deactivateByUserId(UUID userId);

    void validateExistsByEmail(String email);

    Role getRoleByName(RoleName roleName);

    Set<Role> getRolesByName(List<RoleName> roles);

    User getByEmail(String email);
}
