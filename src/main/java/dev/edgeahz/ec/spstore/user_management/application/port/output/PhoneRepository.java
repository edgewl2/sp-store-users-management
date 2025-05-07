package dev.edgeahz.ec.spstore.user_management.application.port.output;

import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository {
    List<Phone> findAllByUserId(Long userId);
    Optional<Phone> findById(Long id);
    Optional<Phone> findByIdAndUserId(Long id, Long userId);
    Phone save(Phone phone);
    void deleteById(Long id);
    void deleteByIdAndUserId(Long id, Long userId);
}
