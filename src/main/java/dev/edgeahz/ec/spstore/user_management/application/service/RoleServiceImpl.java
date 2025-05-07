package dev.edgeahz.ec.spstore.user_management.application.service;

import dev.edgeahz.ec.spstore.user_management.application.port.input.RoleService;
import dev.edgeahz.ec.spstore.user_management.application.port.output.RoleRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public List<Role> getAllRoles() {
        log.info("Obteniendo todos los roles del sistema");

        List<Role> roles = repository.findAll();

        log.info("Se encontraron {} roles en el sistema", roles.size());
        return roles;
    }

    @Override
    public Role getRoleById(Long id) {
        log.info("Buscando rol con ID: {}", id);

        return repository.findById(id)
                .map(role -> {
                    log.info("Rol encontrado: {} (ID: {})", role.getName(), role.getId());
                    return role;
                })
                .orElseThrow(() -> {
                    log.error("No se encontró el rol con ID: {}", id);
                    return new ResourceNotFoundException("Role", id);
                });
    }

    @Override
    public Role createRole(Role role) {
        log.info("Creando nuevo rol: {}", role.getName());

        Role savedRole = repository.save(role);

        log.info("Rol creado exitosamente con ID: {}", savedRole.getId());
        return savedRole;
    }

    @Override
    public Role updateRole(Long id, Role role) {
        log.info("Actualizando rol con ID: {} a: {}", id, role.getName());

        role.setId(id);
        return Optional.ofNullable(repository.save(role))
                .map(updatedRole -> {
                    log.info("Rol actualizado exitosamente: {} (ID: {})", updatedRole.getName(), updatedRole.getId());
                    return updatedRole;
                })
                .orElseThrow(() -> {
                    log.error("Error al actualizar el rol con ID: {}", id);
                    return new ResourceNotFoundException("Role", id);
                });
    }

    @Override
    public void deleteRole(Long id) {
        log.info("Eliminando rol con ID: {}", id);

        repository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se puede eliminar: rol con ID: {} no encontrado", id);
                    return new ResourceNotFoundException("Role", id);
                });

        repository.deleteById(id);
        log.info("Rol con ID: {} eliminado exitosamente", id);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        log.info("Obteniendo roles para el usuario con ID: {}", userId);

        List<Role> roles = repository.findAllByUserId(userId);

        log.info("Se encontraron {} roles para el usuario con ID: {}", roles.size(), userId);
        return roles;
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        log.info("Asignando rol con ID: {} al usuario con ID: {}", roleId, userId);

        Role role = repository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("No se puede asignar: rol con ID: {} no encontrado", roleId);
                    return new ResourceNotFoundException("Role", roleId);
                });

        repository.assignRoleToUser(userId, role.getId());
        log.info("Rol '{}' (ID: {}) asignado exitosamente al usuario con ID: {}", role.getName(), roleId, userId);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        log.info("Removiendo rol con ID: {} del usuario con ID: {}", roleId, userId);

        Role role = repository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("No se puede remover: rol con ID: {} no encontrado", roleId);
                    return new ResourceNotFoundException("Role", roleId);
                });

        repository.removeRoleFromUser(userId, role.getId());
        log.info("Rol '{}' (ID: {}) removido exitosamente del usuario con ID: {}", role.getName(), roleId, userId);
    }
}
