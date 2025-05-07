package dev.edgeahz.ec.spstore.user_management.application.port.output;

import dev.edgeahz.ec.spstore.user_management.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    Role save(Role role);
    void deleteById(Long id);
    List<Role> findAllByUserId(Long userId);
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
