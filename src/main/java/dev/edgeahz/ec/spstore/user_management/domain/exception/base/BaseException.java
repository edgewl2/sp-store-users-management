package dev.edgeahz.ec.spstore.user_management.domain.exception.base;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final String domain;

    protected BaseException(String message, String errorCode, String domain) {
        super(message);
        this.errorCode = errorCode;
        this.domain = domain;
    }

    protected BaseException(String message, String errorCode, String domain, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.domain = domain;
    }
}
