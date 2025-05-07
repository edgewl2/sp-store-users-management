package dev.edgeahz.ec.spstore.user_management.domain.exception;

import dev.edgeahz.ec.spstore.user_management.domain.exception.base.BaseException;

public class BusinessException extends BaseException {
  public BusinessException(String message, String errorCode, String domain) {
    super(message, errorCode, domain);
  }
}
