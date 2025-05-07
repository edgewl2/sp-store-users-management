package dev.edgeahz.ec.spstore.user_management.domain.exception;

public class InvalidTokenException extends BusinessException {
    private static final String ERROR_CODE = "INVALID_TOKEN";

    public InvalidTokenException(String message) {
        super(message, ERROR_CODE, "security");
    }

    public static InvalidTokenException expired() {
        return new InvalidTokenException("El token ha caducado");
    }

    public static InvalidTokenException malformed() {
        return new InvalidTokenException("Formato de token no válido");
    }
}
