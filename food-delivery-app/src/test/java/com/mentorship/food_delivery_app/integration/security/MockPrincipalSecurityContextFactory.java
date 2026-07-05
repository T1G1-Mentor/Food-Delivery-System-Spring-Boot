package com.mentorship.food_delivery_app.integration.security;

import com.mentorship.food_delivery_app.security.entities.CustomerPrincipal;
import com.mentorship.food_delivery_app.security.entities.UserPrincipal;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MockPrincipalSecurityContextFactory implements WithSecurityContextFactory<WithMockPrincipal> {
    @Override
    public SecurityContext createSecurityContext(WithMockPrincipal annotation) {
        List<SimpleGrantedAuthority> roles= Arrays.stream(annotation.roles())
                .map(r-> new SimpleGrantedAuthority(r.name()))
                .toList();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetails userDetails;
        if (annotation.userType().equals(UserType.USER_ADMIN))
            userDetails = new UserPrincipal(UUID.randomUUID(),
                    "",
                    "",
                    "",
                    "",
                    annotation.userType(),
                    roles,
                    true);
        else
            userDetails = new CustomerPrincipal(UUID.randomUUID(),
                    UUID.randomUUID(),
                    "",
                    "",
                    "",
                    "",
                    annotation.userType(),
                    roles,
                    true);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                "",
                roles);
        context.setAuthentication(authentication);


        return context;
    }
}
