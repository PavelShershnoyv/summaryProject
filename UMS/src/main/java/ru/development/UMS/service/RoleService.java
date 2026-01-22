package ru.development.UMS.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.development.UMS.entity.Role;
import ru.development.UMS.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<String> getAllRoleNames() {
        return roleRepository.findAll().stream().map(Role::getRole).toList();
    }

    public Role createRole(String roleName, String description) {
        Optional<Role> exists = roleRepository.findByRoleIgnoreCase(roleName);
        if (exists.isPresent()) {
            throw new DuplicateKeyException("Role with name " + roleName + " already exists");
        }
        Role r = new Role();
        r.setRole(roleName);
        r.setDescription(description);
        return roleRepository.save(r);
    }
}
