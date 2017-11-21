package me.soulyana.security.repositories;

import me.soulyana.security.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    UserRole findByUserRole(String userRole);
}
