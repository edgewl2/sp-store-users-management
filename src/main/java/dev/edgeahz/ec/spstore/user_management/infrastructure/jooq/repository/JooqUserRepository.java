package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.UserRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.DatabaseOperationException;
import dev.edgeahz.ec.spstore.user_management.domain.model.User;
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
public class JooqUserRepository implements UserRepository {

    private final DSLContext dsl;
    private final JooqRoleRepository roleRepository;
    private final JooqAddressRepository addressRepository;
    private final JooqPhoneRepository phoneRepository;

    @Override
    public List<User> findAll() {
        return dsl.selectFrom(Tables.USERS)
                .fetch()
                .stream()
                .map(record -> User.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .birthDate(record.getBirthDate())
                        .enabled(record.getEnabled() != null && record.getEnabled() == 1)
                        .roles(roleRepository.findAllByUserId(record.getId()))
                        .addresses(addressRepository.findAllByUserId(record.getId()))
                        .phones(phoneRepository.findAllByUserId(record.getId()))
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return dsl.selectFrom(Tables.USERS)
                .where(Tables.USERS.ID.eq(id))
                .fetchOptional()
                .map(record -> User.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .birthDate(record.getBirthDate())
                        .enabled(record.getEnabled() != null && record.getEnabled() == 1)
                        .roles(roleRepository.findAllByUserId(record.getId()))
                        .addresses(addressRepository.findAllByUserId(record.getId()))
                        .phones(phoneRepository.findAllByUserId(record.getId()))
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return dsl.selectFrom(Tables.USERS)
                .where(Tables.USERS.USERNAME.eq(username))
                .fetchOptional()
                .map(record -> User.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .birthDate(record.getBirthDate())
                        .enabled(record.getEnabled() != null && record.getEnabled() == 1)
                        .roles(roleRepository.findAllByUserId(record.getId()))
                        .addresses(addressRepository.findAllByUserId(record.getId()))
                        .phones(phoneRepository.findAllByUserId(record.getId()))
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dsl.selectFrom(Tables.USERS)
                .where(Tables.USERS.EMAIL.eq(email))
                .fetchOptional()
                .map(record -> User.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .birthDate(record.getBirthDate())
                        .enabled(record.getEnabled() != null && record.getEnabled() == 1)
                        .roles(roleRepository.findAllByUserId(record.getId()))
                        .addresses(addressRepository.findAllByUserId(record.getId()))
                        .phones(phoneRepository.findAllByUserId(record.getId()))
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public User save(User user) {
        return Optional.ofNullable(dsl.insertInto(Tables.USERS)
                        .set(Tables.USERS.USERNAME, user.getUsername())
                        .set(Tables.USERS.PASSWORD, user.getPassword())
                        .set(Tables.USERS.EMAIL, user.getEmail())
                        .set(Tables.USERS.FIRST_NAME, user.getFirstName())
                        .set(Tables.USERS.LAST_NAME, user.getLastName())
                        .set(Tables.USERS.BIRTH_DATE, user.getBirthDate())
                        .set(Tables.USERS.ENABLED, (byte) (user.isEnabled() ? 1 : 0))
                        .set(Tables.USERS.CREATED_BY, user.getCreatedBy())
                        .set(Tables.USERS.UPDATED_BY, user.getUpdatedBy())
                        .onDuplicateKeyUpdate()
                        .set(Tables.USERS.PASSWORD, user.getPassword())
                        .set(Tables.USERS.EMAIL, user.getEmail())
                        .set(Tables.USERS.FIRST_NAME, user.getFirstName())
                        .set(Tables.USERS.LAST_NAME, user.getLastName())
                        .set(Tables.USERS.BIRTH_DATE, user.getBirthDate())
                        .set(Tables.USERS.ENABLED, (byte) (user.isEnabled() ? 1 : 0))
                        .set(Tables.USERS.UPDATED_BY, user.getUpdatedBy())
                        .set(Tables.USERS.UPDATED_AT, user.getUpdatedAt().atZone(ZoneOffset.UTC).toLocalDateTime())
                        .returning(Tables.USERS.ID)
                        .fetchOne())
                .map(record -> User.builder()
                        .id(record.getId())
                        .username(record.getUsername())
                        .password(record.getPassword())
                        .email(record.getEmail())
                        .firstName(record.getFirstName())
                        .lastName(record.getLastName())
                        .birthDate(record.getBirthDate())
                        .enabled(record.getEnabled() != null && record.getEnabled() == 1)
//                        .roles(roleRepository.findAllByUserId(record.getId()))
//                        .addresses(addressRepository.findAllByUserId(record.getId()))
//                        .phones(phoneRepository.findAllByUserId(record.getId()))
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).orElseThrow(() -> DatabaseOperationException.saveFailed("User"));
    }

    @Override
    public void deleteById(Long id) {
        dsl.deleteFrom(Tables.USERS)
                .where(Tables.USERS.ID.eq(id))
                .execute();
    }

    @Override
    public boolean existsByUsername(String username) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(Tables.USERS)
                        .where(Tables.USERS.USERNAME.eq(username))
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(Tables.USERS)
                        .where(Tables.USERS.EMAIL.eq(email))
        );
    }
}
