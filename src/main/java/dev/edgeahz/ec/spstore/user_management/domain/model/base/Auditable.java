package dev.edgeahz.ec.spstore.user_management.domain.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Auditable<T> {
    private T createdAt;
    private T updatedAt;
    private String createdBy;
    private String updatedBy;
}
