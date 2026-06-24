package com.mentorship.food_delivery_app.security.entities;

import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CustomerPrincipal implements UserDetails {
    private final UUID userId;
    private final UUID customerId;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> roles;
    private final boolean isEnabled;

    public static CustomerPrincipal from(User user, UUID customerId, Collection<? extends GrantedAuthority> roles) {
        return new CustomerPrincipal(
                user.getUserId(),
                customerId,
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserType(),
                roles,
                user.isEnabled()

        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * @return user email
     */
    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
