package ru.development.UMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.development.UMS.entity.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleIgnoreCase(String role);
}
