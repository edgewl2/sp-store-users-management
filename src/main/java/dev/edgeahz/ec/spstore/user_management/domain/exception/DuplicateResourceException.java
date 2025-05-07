package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.ResourceException;

public class DuplicateResourceException extends ResourceException {

    private static final String ERROR_CODE = "DUPLICATE_RESOURCE";

    public DuplicateResourceException(String resourceType, String field, String value) {
        super(String.format("%s ya existe con %s: %s", resourceType, field, value), ERROR_CODE, resourceType);
    }
}
