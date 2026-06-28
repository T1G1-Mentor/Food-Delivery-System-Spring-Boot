package com.mentorship.food_delivery_app.auth.repository;

import com.mentorship.food_delivery_app.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<Otp, UUID> {

    Optional<Otp> findFirstByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String email, LocalDateTime now);

    @Modifying
    void deleteByEmail(String email);

    @Modifying
    void deleteByExpiresAtBefore(LocalDateTime now);
}
