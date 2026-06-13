package com.mentorship.food_delivery_app.user.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.user.entity.Role;
import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import com.mentorship.food_delivery_app.user.exceptions.UserEmailAlreadyExists;
import com.mentorship.food_delivery_app.user.exceptions.UserRoleNotFoundException;
import com.mentorship.food_delivery_app.user.repository.RoleRepository;
import com.mentorship.food_delivery_app.user.repository.UserRepository;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${app.test.user-id}")
    private String userId;

    @Override
    public User getDummyLoggedInUser() {

        return userRepository.findById(UUID.fromString(userId)).get();

    }

    @Transactional
    @Override
    public void deactivateByUserId(UUID userId) {
        log.info("Deactivating account for user with id {}", userId);
        userRepository.deactivateByUserId(userId);
    }

    public void validateExistsByEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new UserEmailAlreadyExists
                    (ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
    }

    @Override
    public Role getRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() ->
                new UserRoleNotFoundException(ErrorMessage.ROLE_NOT_FOUND.getMessage()));
    }

    @Override
    public Set<Role> getRolesByName(List<RoleName> roles) {
        return roleRepository.findAllByName(roles);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("The email you provided does not exist."));
    }
}
