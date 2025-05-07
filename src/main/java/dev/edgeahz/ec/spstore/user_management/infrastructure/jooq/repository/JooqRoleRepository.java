package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.RoleRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.DatabaseOperationException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.Tables;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JooqRoleRepository implements RoleRepository {

    private final DSLContext dsl;

    @Override
    public List<Role> findAll() {
        return dsl.selectFrom(Tables.ROLES)
                .fetch()
                .stream()
                .map(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Role> findById(Long id) {
        return dsl.selectFrom(Tables.ROLES)
                .where(Tables.ROLES.ID.eq(id))
                .fetchOptional()
                .map(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Optional<Role> findByName(String name) {
        return dsl.selectFrom(Tables.ROLES)
                .where(Tables.ROLES.NAME.eq(name))
                .fetchOptional()
                .map(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Role save(Role role) {
        return Optional.ofNullable(dsl.insertInto(Tables.ROLES)
                        .set(Tables.ROLES.NAME, role.getName())
                        .set(Tables.ROLES.DESCRIPTION, role.getDescription())
                        .set(Tables.ROLES.CREATED_BY, role.getCreatedBy())
                        .set(Tables.ROLES.UPDATED_BY, role.getUpdatedBy())
                        .onDuplicateKeyUpdate()
                        .set(Tables.ROLES.NAME, role.getName())
                        .set(Tables.ROLES.DESCRIPTION, role.getDescription())
                        .set(Tables.ROLES.UPDATED_BY, role.getUpdatedBy())
                        .set(Tables.ROLES.UPDATED_AT, role.getUpdatedAt().atOffset(ZoneOffset.UTC).toLocalDateTime())
                        .returning(Tables.ROLES.ID)
                        .fetchOne())
                .map(record -> Role.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).orElseThrow(() -> DatabaseOperationException.saveFailed("Rol"));
    }

    @Override
    public void deleteById(Long id) {
        dsl.deleteFrom(Tables.ROLES)
                .where(Tables.ROLES.ID.eq(id))
                .execute();
    }

    @Override
    public List<Role> findAllByUserId(Long userId) {
        return dsl.select(Tables.ROLES.asterisk())
                .from(Tables.ROLES)
                .join(Tables.USER_ROLES)
                .on(Tables.ROLES.ID.eq(Tables.USER_ROLES.ROLE_ID))
                .where(Tables.USER_ROLES.USER_ID.eq(userId))
                .fetch()
                .stream()
                .map(record -> Role.builder()
                        .id(record.get(Tables.ROLES.ID))
                        .name(record.get(Tables.ROLES.NAME))
                        .description(record.get(Tables.ROLES.DESCRIPTION))
                        .createdBy(record.get(Tables.ROLES.CREATED_BY))
                        .updatedBy(record.get(Tables.ROLES.UPDATED_BY))
                        .createdAt(record.get(Tables.ROLES.CREATED_AT).toInstant(ZoneOffset.UTC))
                        .updatedAt(record.get(Tables.ROLES.UPDATED_AT).toInstant(ZoneOffset.UTC))
                        .build()
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        dsl.insertInto(Tables.USER_ROLES)
                .set(Tables.USER_ROLES.USER_ID, userId)
                .set(Tables.USER_ROLES.ROLE_ID, roleId)
                .execute();
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        dsl.deleteFrom(Tables.USER_ROLES)
                .where(Tables.USER_ROLES.USER_ID.eq(userId)
                        .and(Tables.USER_ROLES.ROLE_ID.eq(roleId)))
                .execute();
    }
}
