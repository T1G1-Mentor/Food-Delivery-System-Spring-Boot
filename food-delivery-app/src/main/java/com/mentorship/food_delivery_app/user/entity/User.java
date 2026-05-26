package com.mentorship.food_delivery_app.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    @Column(name = "user_first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "user_last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "user_birth_date")
    private LocalDate birthDate;

    @Column(name = "user_phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "user_email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_enabled", columnDefinition = "BIT(1)")
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER) // Cant be lazy (required by spring security)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
