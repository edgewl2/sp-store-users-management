package dev.edgeahz.ec.spstore.user_management.application.service;

import dev.edgeahz.ec.spstore.user_management.application.port.input.AddressService;
import dev.edgeahz.ec.spstore.user_management.application.port.output.AddressRepository;
import dev.edgeahz.ec.spstore.user_management.application.port.output.UserRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<Address> getUserAddresses(Long userId) {
        log.info("Obteniendo direcciones para usuario con ID: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        List<Address> addresses = addressRepository.findAllByUserId(userId);
        log.info("Se encontraron {} direcciones para el usuario con ID: {}", addresses.size(), userId);
        return addresses;
    }

    @Override
    public Address getAddressById(Long addressId, Long userId) {
        log.info("Buscando dirección con ID: {} para el usuario con ID: {}", addressId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró la dirección con ID: {} para el usuario con ID: {}", addressId, userId);
                    return new ResourceNotFoundException("Address", addressId);
                });
    }

    @Override
    public Address createAddress(Address address, Long userId) {
        log.info("Guardando dirección: {} , para el usuario con ID: {}", address, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        if (address.isDefault()) {
            unsetDefaultAddresses(userId);
        }

        address.setUserId(userId);
        Address savedAddress = addressRepository.save(address);

        log.info("Dirección guardada con ID: {}", savedAddress.getId());
        return savedAddress;
    }

    @Override
    public Address updateAddress(Long addressId, Long userId, Address addressDetails) {
        log.info("Actualizando dirección con ID: {} para el usuario con ID: {}", addressId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró la dirección con ID: {} para el usuario con ID: {}", addressId, userId);
                    return new ResourceNotFoundException("Address", addressId);
                });

        if (addressDetails.isDefault()) {
            unsetDefaultAddresses(userId);
        }

        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setZipCode(addressDetails.getZipCode());
        address.setCountry(addressDetails.getCountry());
        address.setDefault(addressDetails.isDefault());

        Address updatedAddress = addressRepository.save(address);

        log.info("Dirección actualizada con ID: {}", updatedAddress.getId());
        return updatedAddress;
    }

    @Override
    public void deleteAddress(Long addressId, Long userId) {
        log.info("Eliminando dirección con ID: {} para el usuario con ID: {}", addressId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> {
                    log.error("No se encontró la dirección con ID: {} para el usuario con ID: {}", addressId, userId);
                    return new ResourceNotFoundException("Address", addressId);
                });

        addressRepository.deleteByIdAndUserId(addressId, userId);
        log.info("Dirección con ID: {} eliminada exitosamente", addressId);
    }

    @Override
    public void unsetDefaultAddresses(Long userId) {
        log.info("Desmarcando todas las direcciones como predeterminadas para el usuario con ID: {}", userId);

        List<Address> addresses = addressRepository.findAllByUserId(userId);
        addresses.stream()
                .filter(Address::isDefault)
                .forEach(address -> {
                    address.setDefault(false);
                    addressRepository.save(address);
                });
    }
}
