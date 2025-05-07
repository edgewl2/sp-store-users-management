package dev.edgeahz.ec.spstore.user_management.application.port.output;

import dev.edgeahz.ec.spstore.user_management.domain.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    List<Address> findAllByUserId(Long userId);
    Optional<Address> findById(Long id);
    Optional<Address> findByIdAndUserId(Long id, Long userId);
    Address save(Address address);
    void deleteById(Long id);
    void deleteByIdAndUserId(Long id, Long userId);
}
