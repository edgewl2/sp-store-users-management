package dev.edgeahz.ec.spstore.user_management.application.port.input;

import dev.edgeahz.ec.spstore.user_management.domain.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role getRoleByName(String name);
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    List<Role> getRolesByUserId(Long userId);
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
