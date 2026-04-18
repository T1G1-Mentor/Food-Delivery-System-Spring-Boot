package com.mentorship.food_delivery_app.common.audit;

import com.mentorship.food_delivery_app.user.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorAware")
public class ApplicationAuditAware implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map((user) -> ((User) user).getId());
    }
}
