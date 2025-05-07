package dev.edgeahz.ec.spstore.user_management.application.port.output;

import dev.edgeahz.ec.spstore.user_management.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
    void deleteById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
