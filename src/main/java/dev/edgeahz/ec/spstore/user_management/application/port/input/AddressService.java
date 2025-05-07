package dev.edgeahz.ec.spstore.user_management.application.port.input;

import dev.edgeahz.ec.spstore.user_management.domain.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> getUserAddresses(Long userId);
    Address getAddressById(Long addressId, Long userId);
    Address createAddress(Address address, Long userId);
    Address updateAddress(Long addressId, Long userId, Address addressDetails);
    void deleteAddress(Long addressId, Long userId);
    void unsetDefaultAddresses(Long userId);
}
