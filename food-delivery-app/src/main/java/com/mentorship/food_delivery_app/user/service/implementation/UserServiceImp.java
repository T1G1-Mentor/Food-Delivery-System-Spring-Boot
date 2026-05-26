package com.mentorship.food_delivery_app.user.service.implementation;

import com.mentorship.food_delivery_app.user.entity.User;
import com.mentorship.food_delivery_app.user.repository.UserRepository;
import com.mentorship.food_delivery_app.user.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

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
}
