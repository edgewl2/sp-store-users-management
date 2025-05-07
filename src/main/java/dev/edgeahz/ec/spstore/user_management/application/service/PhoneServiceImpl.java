package dev.edgeahz.ec.spstore.user_management.application.service;

import dev.edgeahz.ec.spstore.user_management.application.port.input.PhoneService;
import dev.edgeahz.ec.spstore.user_management.application.port.output.PhoneRepository;
import dev.edgeahz.ec.spstore.user_management.application.port.output.UserRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    @Override
    public List<Phone> getUserPhones(Long userId) {
        log.info("Obteniendo teléfonos para el usuario con ID: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        List<Phone> phones = phoneRepository.findAllByUserId(userId);
        log.info("Se encontraron {} teléfonos para el usuario con ID: {}", phones.size(), userId);
        return phones;
    }

    @Override
    public Phone getPhoneById(Long phoneId, Long userId) {
        log.info("Buscando teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        return phoneRepository.findByIdAndUserId(phoneId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);
                    return new ResourceNotFoundException("Phone", phoneId);
                });
    }

    @Override
    public Phone createPhone(Phone phone, Long userId) {
        log.info("Guardando teléfono: {} para el usuario con ID: {}", phone, userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        if (phone.isDefault()) {
            unsetDefaultPhones(userId);
        }

        phone.setUserId(userId);
        Phone phoneSaved = phoneRepository.save(phone);

        log.info("Teléfono guardado con ID: {}", phoneSaved.getId());
        return phoneSaved;
    }

    @Override
    public Phone updatePhone(Long phoneId, Long userId, Phone phoneDetails) {
        log.info("Actualizando teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Phone phone = phoneRepository.findByIdAndUserId(phoneId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);
                    return new ResourceNotFoundException("Phone", phoneId);
                });

        if (phoneDetails.isDefault()) {
            unsetDefaultPhones(userId);
        }

        phone.setType(phoneDetails.getType());
        phone.setCountryCode(phoneDetails.getCountryCode());
        phone.setNumber(phoneDetails.getNumber());
        phone.setDefault(phoneDetails.isDefault());

        Phone updatedPhone = phoneRepository.save(phone);

        log.info("Teléfono actualizado con ID: {}", updatedPhone.getId());
        return updatedPhone;
    }

    @Override
    public void deletePhone(Long phoneId, Long userId) {
        log.info("Eliminando teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        phoneRepository.findByIdAndUserId(phoneId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el teléfono con ID: {} para el usuario con ID: {}", phoneId, userId);
                    return new ResourceNotFoundException("Phone", phoneId);
                });

        phoneRepository.deleteByIdAndUserId(phoneId, userId);
        log.info("Teléfono con ID: {} eliminado exitosamente", phoneId);
    }

    @Override
    public void unsetDefaultPhones(Long userId) {
        log.info("Desmarcando teléfonos por defecto para el usuario con ID: {}", userId);

        List<Phone> defaultPhones = phoneRepository.findAllByUserId(userId);
        defaultPhones.stream()
                .filter(Phone::isDefault)
                .forEach(phone -> {
                    phone.setDefault(false);
                    phoneRepository.save(phone);
                });
    }
}
