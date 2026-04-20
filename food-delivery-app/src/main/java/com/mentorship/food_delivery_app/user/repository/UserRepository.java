package com.mentorship.food_delivery_app.user.repository;

import com.mentorship.food_delivery_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
