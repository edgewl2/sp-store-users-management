package dev.edgeahz.ec.spstore.user_management.domain.exception;

public class PasswordMismatchException extends BusinessException {
    private static final String ERROR_CODE = "PASSWORD_MISMATCH";

    public PasswordMismatchException(String message) {
        super(message, ERROR_CODE, "user");
    }

    public static PasswordMismatchException currentPasswordIncorrect() {
        return new PasswordMismatchException("La contraseña actual es incorrecta");
    }

    public static PasswordMismatchException passwordTooWeak() {
        return new PasswordMismatchException("La contraseña no cumple con los requisitos de seguridad");
    }
}
