package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.PhoneRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.DatabaseOperationException;
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
        return dsl.selectFrom(Tables.PHONES)
                .where(Tables.PHONES.ID.eq(id))
                .fetchOptional()
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
                );
    }

    @Override
    public Optional<Phone> findByIdAndUserId(Long id, Long userId) {
        return dsl.selectFrom(Tables.PHONES)
                .where(Tables.PHONES.ID.eq(id))
                .and(Tables.PHONES.USER_ID.eq(userId))
                .fetchOptional()
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
                );
    }

    @Override
    public Phone save(Phone phone) {
        return Optional.ofNullable(dsl.insertInto(Tables.PHONES)
                        .set(Tables.PHONES.USER_ID, phone.getUserId())
                        .set(Tables.PHONES.TYPE, Tables.PHONES.TYPE.getDataType().convert(phone.getType().name()))
                        .set(Tables.PHONES.COUNTRY_CODE, phone.getCountryCode())
                        .set(Tables.PHONES.NUMBER, phone.getNumber())
                        .set(Tables.PHONES.IS_DEFAULT, (byte) (phone.isDefault() ? 1 : 0))
                        .returning()
                        .fetchOne()
                ).map(record -> Phone.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .type(Phone.PhoneType.valueOf(record.getType().name()))
                        .countryCode(record.getCountryCode())
                        .number(record.getNumber())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).orElseThrow(() -> DatabaseOperationException.saveFailed("Phones"));
    }

    @Override
    public void deleteById(Long id) {
        dsl.deleteFrom(Tables.PHONES)
                .where(Tables.PHONES.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        dsl.deleteFrom(Tables.PHONES)
                .where(Tables.PHONES.ID.eq(id))
                .and(Tables.PHONES.USER_ID.eq(userId))
                .execute();
    }
}
