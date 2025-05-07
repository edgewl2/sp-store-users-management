package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.PhoneRepository;
import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;
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
public class JooqPhoneRepository implements PhoneRepository {

    private final DSLContext dsl;

    @Override
    public List<Phone> findAllByUserId(Long userId) {
        return dsl.selectFrom(Tables.PHONES)
                .where(Tables.PHONES.USER_ID.eq(userId))
                .fetch()
                .stream()
                .map(record -> Phone.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .type(Phone.PhoneType.valueOf(record.getType().name()))
                        .countryCode(record.getCountryCode())
                        .number(record.getNumber())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Phone> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Phone> findByIdAndUserId(Long id, Long userId) {
        return Optional.empty();
    }

    @Override
    public Phone save(Phone phone) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {

    }
}
