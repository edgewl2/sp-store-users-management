package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.BaseException;

public class AuthenticationException extends BaseException {
    private static final String ERROR_CODE = "AUTHENTICATION_ERROR";

    public AuthenticationException(String message) {
        super(message, ERROR_CODE, "security");
    }

    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Usuario o contraseña incorrectos");
    }

    public static AuthenticationException accountLocked() {
        return new AuthenticationException("Cuenta bloqueada");
    }

    public static AuthenticationException accountDisabled() {
        return new AuthenticationException("Cuenta deshabilitada");
    }
}
