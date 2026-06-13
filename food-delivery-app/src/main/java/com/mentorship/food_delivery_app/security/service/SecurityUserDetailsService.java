package com.mentorship.food_delivery_app.security.service;

import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.security.entities.SecurityCustomer;
import com.mentorship.food_delivery_app.security.entities.SecurityUser;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getByEmail(email);

        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();

        if (user.getUserType().equals(UserType.USER_ADMIN))
            return SecurityUser.from(user, roles);

        UUID customerId = customerService.getCustomerIdByUserId(user.getUserId());

        return SecurityCustomer.from(user, customerId, roles);
    }
}
