package dev.edgeahz.ec.spstore.user_management.shared.mapper;

import dev.edgeahz.ec.spstore.user_management.api.rest.dto.RoleRequest;
import dev.edgeahz.ec.spstore.user_management.api.rest.dto.RoleResponse;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    Role toRole(RoleRequest roleRequest);

    default OffsetDateTime map(Instant value) {
        return value != null ? OffsetDateTime.ofInstant(value, OffsetDateTime.now(ZoneOffset.UTC).getOffset()) : null;
    }
}
