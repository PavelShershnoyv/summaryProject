package ru.development.UMS.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.development.UMS.dto.User;
import ru.development.UMS.entity.Role;
import ru.development.UMS.entity.UserEntity;
import ru.development.UMS.repository.RoleRepository;
import ru.development.UMS.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(User user) {
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setDate(user.getDate());

        if (user.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : user.getRoles()) {
                roleRepository.findByRoleIgnoreCase(roleName).ifPresent(roles::add);
            }
            entity.setRoles(roles);
        }

        UserEntity saved = userRepository.save(entity);
        return toDto(saved);
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setDate(user.getDate());
        if (user.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : user.getRoles()) {
                roleRepository.findByRoleIgnoreCase(roleName).ifPresent(roles::add);
            }
            entity.setRoles(roles);
        }
        UserEntity saved = userRepository.save(entity);
        return toDto(saved);
    }

    public Optional<User> getById(Integer id) {
        return userRepository.findById(id).map(this::toDto);
    }

    public List<User> getAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public void delete(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }
        userRepository.delete(user);
    }

    public User assignRole(Integer userId, String roleName) {
        UserEntity entity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleRepository.findByRoleIgnoreCase(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Set<Role> roles = entity.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            entity.setRoles(roles);
        }
        roles.add(role);
        UserEntity saved = userRepository.save(entity);
        return toDto(saved);
    }

    private User toDto(UserEntity entity) {
        User u = new User();
        u.setId(entity.getId());
        u.setUsername(entity.getUsername());
        u.setEmail(entity.getEmail());
        u.setFirstName(entity.getFirstName());
        u.setLastName(entity.getLastName());
        u.setDate(entity.getDate());

        if (entity.getRoles() != null) {
            u.setRoles(entity.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));
        }

        return u;
    }
}
