package dev.edgeahz.ec.spstore.user_management.domain.model;

import dev.edgeahz.ec.spstore.user_management.domain.model.base.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
public class Phone extends Auditable<Instant> {
    Long id;
    Long userId;
    String number;
    String countryCode;
    PhoneType type;
    boolean isDefault;

    public enum PhoneType {
        MOBILE, HOME, WORK, OTHER
    }
}
