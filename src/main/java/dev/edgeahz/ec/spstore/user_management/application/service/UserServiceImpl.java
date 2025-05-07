package dev.edgeahz.ec.spstore.user_management.application.service;

import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.application.port.input.AddressService;
import dev.edgeahz.ec.spstore.user_management.application.port.input.PhoneService;
import dev.edgeahz.ec.spstore.user_management.application.port.input.RoleService;
import dev.edgeahz.ec.spstore.user_management.application.port.input.UserService;
import dev.edgeahz.ec.spstore.user_management.application.port.output.UserRepository;
import dev.edgeahz.ec.spstore.user_management.domain.exception.BusinessException;
import dev.edgeahz.ec.spstore.user_management.domain.exception.DuplicateResourceException;
import dev.edgeahz.ec.spstore.user_management.domain.exception.ResourceNotFoundException;
import dev.edgeahz.ec.spstore.user_management.domain.model.Address;
import dev.edgeahz.ec.spstore.user_management.domain.model.Phone;
import dev.edgeahz.ec.spstore.user_management.domain.model.Role;
import dev.edgeahz.ec.spstore.user_management.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final int MINIMUM_AGE = 18;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AddressService addressService;
    private final PhoneService phoneService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        log.info("Obteniendo todos los usuarios del sistema");
        List<User> users = userRepository.findAll();

        log.info("Se encontraron {} usuarios en el sistema", users.size());
        return users;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new ResourceNotFoundException("User", id);
                });
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Buscando usuario con nombre de usuario: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con nombre de usuario: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Buscando usuario con correo electrónico: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con correo electrónico: {}", email);
                    return new ResourceNotFoundException("User", "email", email);
                });
    }

    @Override
    public User createUser(User user) {
        log.info("Creando nuevo usuario: {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("El usuario ya existe");
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("El correo electrónico ya está en uso");
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }

        // Verificar si el usuario es mayor de edad
        if (!isAdult(user.getBirthDate())) {
            log.error("El usuario debe ser mayor de {} años", MINIMUM_AGE);
            throw new BusinessException("El usuario debe ser mayor de " + MINIMUM_AGE + " años", "AGE_RESTRICTION", "user");
        }

        User userToSave = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .enabled(true)
                .roles(Collections.emptyList())
                .addresses(Collections.emptyList())
                .phones(Collections.emptyList())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        User savedUser = userRepository.save(userToSave);

        // Asignar rol USER por defecto
        try {
            Role userRole = roleService.getRoleByName("USER");
            roleService.assignRoleToUser(savedUser.getId(), userRole.getId());
        } catch (ResourceNotFoundException e) {
            log.error("No se encontró el usuario con ID: {}", userToSave.getId());
        }

        // Cargar usuario completo con relaciones
        return getUserById(savedUser.getId());
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        log.info("Actualizando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new ResourceNotFoundException("User", id);
                });

        if (!user.getUsername().equals(userDetails.getUsername()) && userRepository.existsByUsername(userDetails.getUsername())) {
            log.error("El nombre de usuario ya está en uso");
            throw new DuplicateResourceException("User", "username", userDetails.getUsername());
        }

        if (!user.getEmail().equals(userDetails.getEmail()) && userRepository.existsByEmail(userDetails.getEmail())) {
            log.error("El correo electrónico ya está en uso");
            throw new DuplicateResourceException("User", "email", userDetails.getEmail());
        }

        // Verificar si el usuario es mayor de edad
        if (!isAdult(userDetails.getBirthDate())) {
            log.error("El usuario debe ser mayor de {} años", MINIMUM_AGE);
            throw new BusinessException("El usuario debe ser mayor de " + MINIMUM_AGE + " años", "AGE_RESTRICTION", "user");
        }

        user.setId(id);
        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setEmail(userDetails.getEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setBirthDate(userDetails.getBirthDate());
        user.setEnabled(true);
        user.setUpdatedAt(Instant.now());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        getUserById(id);

        userRepository.deleteById(id);
        log.info("Usuario con ID: {} eliminado", id);
    }

    @Override
    public void changePassword(Long id, String currentPassword, String newPassword) {
        log.info("Cambiando contraseña para el usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new ResourceNotFoundException("User", id);
                });

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.error("La contraseña actual es incorrecta");
            throw new BusinessException("La contraseña actual es incorrecta", "INVALID_PASSWORD", "user");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Contraseña cambiada exitosamente para el usuario con ID: {}", id);
    }

    @Override
    public Address addAddress(Long userId, Address address) {
        log.info("Agregando dirección para el usuario con ID: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Address addressSaved = addressService.createAddress(address, userId);

        log.info("Dirección guardada con ID: {}", addressSaved.getId());
        return addressSaved;
    }

    @Override
    public Phone addPhone(Long userId, Phone phone) {
        log.info("Agregando teléfono para el usuario con ID: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Phone phoneSaved = phoneService.createPhone(phone, userId);

        log.info("Teléfono guardado con ID: {}", phoneSaved.getId());
        return phoneSaved;
    }

    @Override
    public void assignRole(Long userId, Long roleId) {
        log.info("Asignando rol con ID: {} al usuario con ID: {}", roleId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Role role = roleService.getRoleById(roleId);

        if (user.getRoles().contains(role)) {
            log.error("El rol ya está asignado al usuario");
            throw new DuplicateResourceException("User", "role", role.getName());
        }

        user.getRoles().add(role);
        userRepository.save(user);
        log.info("Rol con ID: {} asignado al usuario con ID: {}", roleId, userId);
    }

    @Override
    public void removeRole(Long userId, Long roleId) {
        log.info("Removiendo rol con ID: {} del usuario con ID: {}", roleId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        Role role = roleService.getRoleById(roleId);

        if (!user.getRoles().contains(role)) {
            log.error("El rol no está asignado al usuario");
            throw new ResourceNotFoundException("User", "role", role.getName());
        }

        user.getRoles().remove(role);
        userRepository.save(user);
        log.info("Rol con ID: {} removido del usuario con ID: {}", roleId, userId);
    }

    @Override
    public User createCompleteUser(User user, List<Address> addresses, List<Phone> phones, List<Long> roleIds) {
        // Crear usuario
        User createdUser = createUser(user);

        // Agregar direcciones
        if (addresses != null && !addresses.isEmpty()) {
            for (Address address : addresses) {
                addressService.createAddress(address, createdUser.getId());
            }
        }

        // Agregar teléfonos
        if (phones != null && !phones.isEmpty()) {
            for (Phone phone : phones) {
                phoneService.createPhone(phone, createdUser.getId());
            }
        }

        // Asignar roles adicionales
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                roleService.assignRoleToUser(createdUser.getId(), roleId);
            }
        }

        // Cargar usuario completo con relaciones
        return getUserById(createdUser.getId());
    }

    /**
     * Verifica si una persona es mayor de edad según su fecha de nacimiento.
     *
     * @param birthDate Fecha de nacimiento
     * @return true si es mayor de edad, false en caso contrario
     */
    private boolean isAdult(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= MINIMUM_AGE;
    }
}
