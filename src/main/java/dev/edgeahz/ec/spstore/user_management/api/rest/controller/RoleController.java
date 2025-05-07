package dev.edgeahz.ec.spstore.user_management.api.rest.controller;

import dev.edgeahz.ec.spstore.user_management.api.rest.RoleApi;
import dev.edgeahz.ec.spstore.user_management.api.rest.dto.RoleRequest;
import dev.edgeahz.ec.spstore.user_management.api.rest.dto.RoleResponse;
import dev.edgeahz.ec.spstore.user_management.application.port.input.RoleService;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import dev.edgeahz.ec.spstore.user_management.shared.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController implements RoleApi {

    private final RoleService service;
    private final RoleMapper mapper;

    @Override
    public ResponseEntity<RoleResponse> createRole(RoleRequest roleRequest) {
        Role role = mapper.toRole(roleRequest);
        RoleResponse roleResponse = mapper.toRoleResponse(service.createRole(role));
        return ResponseEntity.ok(roleResponse);
    }

    @Override
    public ResponseEntity<Void> deleteRole(String id) {
        service.deleteRole(Long.parseLong(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RoleResponse>> getAllRoles() {

        List<RoleResponse> roles = service.getAllRoles()
                .stream()
                .map(mapper::toRoleResponse)
                .toList();

        return ResponseEntity.ok(roles);
    }

    @Override
    public ResponseEntity<RoleResponse> getRoleById(String id) {
        Long roleId = Long.parseLong(id);
        RoleResponse roleResponse = mapper.toRoleResponse(service.getRoleById(roleId));
        return ResponseEntity.ok(roleResponse);
    }

    @Override
    public ResponseEntity<RoleResponse> updateRole(String id, RoleRequest roleRequest) {
        Long roleId = Long.parseLong(id);
        Role role = mapper.toRole(roleRequest);
        RoleResponse roleResponse = mapper.toRoleResponse(service.updateRole(roleId, role));
        return ResponseEntity.ok(roleResponse);
    }
}
