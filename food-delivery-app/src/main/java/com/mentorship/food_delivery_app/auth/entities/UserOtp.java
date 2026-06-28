package com.mentorship.food_delivery_app.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.sql.ast.tree.expression.Star;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.UUID;

@Entity
@Table(name = "user_otp")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class UserOtp {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "user_otp_id")
    private UUID otpId;

    @Column(name = "user_otp_user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_otp_user_email", nullable = false, length=50)
    private String userEmail;

    @Column(name = "otp_code", nullable = false, length = 64)
    private String otpCode;

    @Column(name = "user_otp_expiration", nullable = false)
    private LocalDateTime otpExpiration;

    @Column(name = "user_otp_revoked")
    private boolean isRevoked;

    public boolean isExpired(){
        return otpExpiration.isBefore(LocalDateTime.now());
    }
}
