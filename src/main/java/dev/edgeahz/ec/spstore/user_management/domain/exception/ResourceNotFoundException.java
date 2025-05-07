package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.ResourceException;

import java.util.UUID;

public class ResourceNotFoundException extends ResourceException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String resourceType, Long id) {
        super(String.format("%s no fue encontrado con id: %s", resourceType, id), ERROR_CODE, resourceType);
    }

    public ResourceNotFoundException(String resourceType, String field, String value) {
        super(String.format("%s no fue encontrado con %s: %s", resourceType, field, value), ERROR_CODE, resourceType);
    }
}
