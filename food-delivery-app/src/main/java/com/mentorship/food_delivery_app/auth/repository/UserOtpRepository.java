package com.mentorship.food_delivery_app.auth.repository;

import com.mentorship.food_delivery_app.auth.entities.UserOtp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserOtpRepository extends CrudRepository<UserOtp, UUID> {

    @Query("""
    SELECT uo FROM UserOtp uo
    WHERE uo.userEmail = :email AND uo.isRevoked = false
""")
    Optional<UserOtp> findByUserEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE UserOtp SET isRevoked = true WHERE userEmail = :email
""")
    void revokeUserOtpByEmail(String email);
}
