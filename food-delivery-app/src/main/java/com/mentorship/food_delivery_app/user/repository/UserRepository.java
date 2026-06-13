package com.mentorship.food_delivery_app.user.repository;

import com.mentorship.food_delivery_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
                UPDATE User SET isEnabled = false
            """)
    void deactivateByUserId(UUID userId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("""
                SELECT u FROM User u
                JOIN FETCH u.roles
                WHERE u.email = :email
            """)
    Optional<User> findByEmail(String email);
}
