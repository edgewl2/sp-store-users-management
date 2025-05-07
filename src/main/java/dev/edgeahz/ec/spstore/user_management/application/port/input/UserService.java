package dev.edgeahz.ec.spstore.user_management.application.port.input;

import dev.edgeahz.ec.spstore.user_management.domain.model.Address;
import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;
import dev.edgeahz.ec.spstore.user_management.domain.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    void changePassword(Long id, String currentPassword, String newPassword);
    Address addAddress(Long userId, Address address);
    Phone addPhone(Long userId, Phone phone);
    void assignRole(Long userId, Long roleId);
    void removeRole(Long userId, Long roleId);
    User createCompleteUser(User user, List<Address> addresses, List<Phone> phones, List<Long> roleIds);
}
