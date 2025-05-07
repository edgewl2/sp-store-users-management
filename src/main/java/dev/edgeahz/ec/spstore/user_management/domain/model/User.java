package dev.edgeahz.ec.spstore.user_management.domain.model;

import dev.edgeahz.ec.spstore.user_management.domain.model.base.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class User extends Auditable<Instant> {
    Long id;
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
    LocalDate birthDate;
    boolean enabled;
    List<Role> roles;
    List<Address> addresses;
    List<Phone> phones;
}
