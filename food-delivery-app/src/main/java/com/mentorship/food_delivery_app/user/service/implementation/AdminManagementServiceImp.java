package com.mentorship.food_delivery_app.user.service.implementation;

import com.mentorship.food_delivery_app.security.entities.UserPrincipal;
import com.mentorship.food_delivery_app.security.service.JwtService;
import com.mentorship.food_delivery_app.user.dto.request.AdminCreationDto;
import com.mentorship.food_delivery_app.user.entity.Role;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
import com.mentorship.food_delivery_app.user.repository.UserRepository;
import com.mentorship.food_delivery_app.user.service.contract.AdminManagementService;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdminManagementServiceImp implements AdminManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    @Override
    public String createAdmin(AdminCreationDto adminCreationDto) {
        userService.validateExistsByEmail(adminCreationDto.email());
        Set<Role> roles = userService.getRolesByName(adminCreationDto.
                roles()
        );

        User user = buildUser(adminCreationDto,
                UserType.USER_ADMIN,
                roles,
                passwordEncoder.encode(adminCreationDto.password()));


        User savedUser = userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.from(savedUser,
                roles.stream().map(role ->
                        new SimpleGrantedAuthority(role.getRoleName()
                                .name())).toList());

        return jwtService.generateToken(userPrincipal);
    }

    private User buildUser(AdminCreationDto registrationDto, UserType userType,
                           Set<Role> roles, String encodedPassword) {
        return User.builder()
                .userType(userType)
                .firstName(registrationDto.firstName())
                .lastName(registrationDto.lastName())
                .birthDate(registrationDto.birthDate())
                .phone(registrationDto.phone())
                .email(registrationDto.email())
                .password(encodedPassword)
                .isEnabled(true)
                .roles(roles).build();
    }
}
