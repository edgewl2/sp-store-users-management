package dev.edgeahz.ec.spstore.user_management.domain.exception.base;

public abstract class ResourceException extends BaseException {

    protected ResourceException(String message, String errorCode, String domain) {
        super(message, errorCode, domain);
    }
}
