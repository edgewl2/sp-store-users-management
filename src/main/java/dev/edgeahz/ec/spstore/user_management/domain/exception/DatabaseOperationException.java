package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.BaseException;

public class DatabaseOperationException extends BaseException {
    private static final String ERROR_CODE = "DATABASE_OPERATION_ERROR";

    public DatabaseOperationException(String message, String domain) {
        super(message, ERROR_CODE, domain);
    }

    public static DatabaseOperationException saveFailed(String entityType) {
        return new DatabaseOperationException(
                String.format("No se pudo guardar el %s en la base de datos", entityType),
                entityType.toLowerCase()
        );
    }

    public static DatabaseOperationException updateFailed(String entityType, String id) {
        return new DatabaseOperationException(
                String.format("No se pudo actualizar el %s con id: %s", entityType, id),
                entityType.toLowerCase()
        );
    }

    public static DatabaseOperationException deleteFailed(String entityType, String id) {
        return new DatabaseOperationException(
                String.format("No se pudo eliminar el %s con id: %s", entityType, id),
                entityType.toLowerCase()
        );
    }
}
