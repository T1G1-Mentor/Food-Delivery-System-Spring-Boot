package com.mentorship.food_delivery_app.integration.security;

import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockPrincipalSecurityContextFactory.class)
public @interface WithMockPrincipal {
    UserType userType() default UserType.USER_ADMIN;

    RoleName[] roles() default {RoleName.ROLE_ADMIN};
}
