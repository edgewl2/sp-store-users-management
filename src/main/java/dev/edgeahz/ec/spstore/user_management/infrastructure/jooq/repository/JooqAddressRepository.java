package dev.edgeahz.ec.spstore.user_management.infrastructure.jooq.repository;

import dev.edgeahz.ec.spstore.user_management.application.port.output.AddressRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.DatabaseOperationException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Address;
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
public class JooqAddressRepository implements AddressRepository {

    private final DSLContext dsl;

    @Override
    public List<Address> findAllByUserId(Long userId) {
        return dsl.selectFrom(Tables.ADDRESSES)
                .where(Tables.ADDRESSES.USER_ID.eq(userId))
                .fetch()
                .stream()
                .map(record -> Address.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .street(record.getStreet())
                        .city(record.getCity())
                        .state(record.getState())
                        .country(record.getCountry())
                        .zipCode(record.getZipCode())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .label(record.getLabel())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Address> findById(Long id) {
        return dsl.selectFrom(Tables.ADDRESSES)
                .where(Tables.ADDRESSES.ID.eq(id))
                .fetchOptional()
                .map(record -> Address.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .street(record.getStreet())
                        .city(record.getCity())
                        .state(record.getState())
                        .country(record.getCountry())
                        .zipCode(record.getZipCode())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .label(record.getLabel())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Optional<Address> findByIdAndUserId(Long id, Long userId) {
        return dsl.selectFrom(Tables.ADDRESSES)
                .where(Tables.ADDRESSES.ID.eq(id))
                .and(Tables.ADDRESSES.USER_ID.eq(userId))
                .fetchOptional()
                .map(record -> Address.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .street(record.getStreet())
                        .city(record.getCity())
                        .state(record.getState())
                        .country(record.getCountry())
                        .zipCode(record.getZipCode())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .label(record.getLabel())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                );
    }

    @Override
    public Address save(Address address) {
        return Optional.ofNullable(dsl.insertInto(Tables.ADDRESSES)
                        .set(Tables.ADDRESSES.USER_ID, address.getUserId())
                        .set(Tables.ADDRESSES.STREET, address.getStreet())
                        .set(Tables.ADDRESSES.CITY, address.getCity())
                        .set(Tables.ADDRESSES.STATE, address.getState())
                        .set(Tables.ADDRESSES.COUNTRY, address.getCountry())
                        .set(Tables.ADDRESSES.ZIP_CODE, address.getZipCode())
                        .set(Tables.ADDRESSES.IS_DEFAULT, (byte) (address.isDefault() ? 1 : 0))
                        .set(Tables.ADDRESSES.LABEL, address.getLabel())
                        .returning(Tables.ADDRESSES.ID)
                        .fetchOne())
                .map(record -> Address.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .street(record.getStreet())
                        .city(record.getCity())
                        .state(record.getState())
                        .country(record.getCountry())
                        .zipCode(record.getZipCode())
                        .isDefault(record.getIsDefault() != null && record.getIsDefault() == 1)
                        .label(record.getLabel())
                        .createdBy(record.getCreatedBy())
                        .updatedBy(record.getUpdatedBy())
                        .createdAt(record.getCreatedAt().toInstant(ZoneOffset.UTC))
                        .updatedAt(record.getUpdatedAt().toInstant(ZoneOffset.UTC))
                        .build()
                ).orElseThrow(() -> DatabaseOperationException.saveFailed("Address"));
    }

    @Override
    public void deleteById(Long id) {
        dsl.deleteFrom(Tables.ADDRESSES)
                .where(Tables.ADDRESSES.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        dsl.deleteFrom(Tables.ADDRESSES)
                .where(Tables.ADDRESSES.ID.eq(id))
                .and(Tables.ADDRESSES.USER_ID.eq(userId))
                .execute();
    }
}
