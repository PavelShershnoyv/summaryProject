package ru.development.UMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.development.UMS.entity.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select r.role from UserEntity u join u.roles r where u.id = :userId")
    Set<String> getRolesUser(@Param("userId") Integer userId);
}
