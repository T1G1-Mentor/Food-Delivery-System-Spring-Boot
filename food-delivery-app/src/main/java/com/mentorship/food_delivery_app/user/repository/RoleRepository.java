package com.mentorship.food_delivery_app.user.repository;

import com.mentorship.food_delivery_app.user.entity.Role;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("""
               SELECT r FROM Role r
                WHERE r.roleName = :roleName
            """)
    Optional<Role> findByName(RoleName roleName);

    @Query("""
                SELECT r FROM Role r
                WHERE r.roleName IN :roles
            """)
    Set<Role> findAllByName(List<RoleName> roles);
}
