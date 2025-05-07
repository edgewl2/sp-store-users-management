package dev.edgeahz.ec.spstore.user_management.domain.model;

import dev.edgeahz.ec.spstore.user_management.domain.model.base.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
public class Role extends Auditable<Instant> {
    Long id;
    String name;
    String description;
}
