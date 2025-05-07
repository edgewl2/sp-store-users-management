package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.BaseException;

public class AuthorizationException extends BaseException {
    private static final String ERROR_CODE = "AUTHORIZATION_ERROR";

    public AuthorizationException(String message) {
        super(message, ERROR_CODE, "security");
    }

    public static AuthorizationException insufficientPermissions() {
        return new AuthorizationException("Permisos insuficientes para realizar esta operación");
    }

    public static AuthorizationException resourceAccessDenied(String resourceType, String resourceId) {
        return new AuthorizationException(
                String.format("Acceso denegado a %s con id: %s", resourceType, resourceId)
        );
    }
}
