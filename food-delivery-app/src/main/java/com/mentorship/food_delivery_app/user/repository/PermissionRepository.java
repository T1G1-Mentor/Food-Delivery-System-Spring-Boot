package com.mentorship.food_delivery_app.user.repository;

import com.mentorship.food_delivery_app.user.entity.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {
}
