package dev.edgeahz.ec.spstore.user_management.application.port.input;

import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;

import java.util.List;

public interface PhoneService {
    List<Phone> getUserPhones(Long userId);
    Phone getPhoneById(Long phoneId, Long userId);
    Phone createPhone(Phone phone, Long userId);
    Phone updatePhone(Long phoneId, Long userId, Phone phoneDetails);
    void deletePhone(Long phoneId, Long userId);
    void unsetDefaultPhones(Long userId);
}
