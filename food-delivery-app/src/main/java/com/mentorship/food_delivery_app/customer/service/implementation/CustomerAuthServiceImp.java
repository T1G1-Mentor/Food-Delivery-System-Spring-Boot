package com.mentorship.food_delivery_app.customer.service.implementation;

import com.mentorship.food_delivery_app.auth.dto.CustomerRegistrationDto;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.repository.CustomerRepository;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerAuthService;
import com.mentorship.food_delivery_app.security.entities.SecurityCustomer;
import com.mentorship.food_delivery_app.security.service.JwtService;
import com.mentorship.food_delivery_app.user.entity.Role;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerAuthServiceImp implements CustomerAuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    @Override
    public String register(CustomerRegistrationDto customerRegistrationDto) {
        userService.validateExistsByEmail(customerRegistrationDto.email());
        Set<Role> roles = Set.of(userService.getRoleByName(RoleName.ROLE_CUSTOMER));

        User user = buildUser(customerRegistrationDto,
                UserType.CUSTOMER,
                roles,
                passwordEncoder.encode(customerRegistrationDto.password()));

        Customer customer = Customer.builder().user(user).build();
        Customer savedCustomer = customerRepository.save(customer);

        SecurityCustomer securityCustomer = SecurityCustomer.from(user,
                savedCustomer.getCustomerId(),
                roles.stream().map(role ->
                        new SimpleGrantedAuthority(role.getRoleName()
                                .name())).toList());

        return jwtService.generateToken(securityCustomer);
    }

    private User buildUser(CustomerRegistrationDto customerRegistrationDto, UserType userType,
                           Set<Role> roles, String encodedPassword) {
        return User.builder()
                .userType(userType)
                .firstName(customerRegistrationDto.firstName())
                .lastName(customerRegistrationDto.lastName())
                .birthDate(customerRegistrationDto.birthDate())
                .phone(customerRegistrationDto.phone())
                .email(customerRegistrationDto.email())
                .password(encodedPassword)
                .isEnabled(true)
                .roles(roles).build();
    }
}
