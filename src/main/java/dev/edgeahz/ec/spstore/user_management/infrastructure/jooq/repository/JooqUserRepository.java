package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.UserRepository;
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
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
