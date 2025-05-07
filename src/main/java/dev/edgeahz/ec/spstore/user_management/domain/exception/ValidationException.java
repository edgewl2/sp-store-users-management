package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.BaseException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends BaseException {
    private static final String ERROR_CODE = "VALIDATION_ERROR";
    private final List<ValidationError> errors;

    public ValidationException(String message, List<ValidationError> errors) {
        super(message, ERROR_CODE, "validation");
        this.errors = errors;
    }

    public static ValidationException of(String field, String message) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError(field, message));
        return new ValidationException("Validación fallida", errors);
    }

    public record ValidationError(String field, String message) {
    }
}
