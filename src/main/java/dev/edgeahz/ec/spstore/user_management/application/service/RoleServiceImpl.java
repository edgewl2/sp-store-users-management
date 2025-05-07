package dev.edgeahz.ec.spstore.user_management.application.service;

import dev.edgeahz.ec.spstore.user_management.application.port.input.RoleService;
import dev.edgeahz.ec.spstore.user_management.application.port.output.RoleRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public List<Role> getAllRoles() {
        return repository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    @Override
    public Role createRole(Role role) {
        return repository.save(role);
    }

    @Override
    public Role updateRole(Long id, Role role) {
        role.setId(id);
        return Optional.ofNullable(repository.save(role))
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    @Override
    public void deleteRole(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        repository.deleteById(id);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        repository.assignRoleToUser(userId, role.getId());
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        repository.removeRoleFromUser(userId, role.getId());
    }
}
