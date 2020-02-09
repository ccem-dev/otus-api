package org.ccem.otus.exceptions;

public interface ResponseError {

  Object getObjectError();

  enum ErrorType {
    ALREADY_EXIST, DATA_NOT_FOUND, OBJECT_INVALID, INVALID_PASSWORD, EMAIL_NOT_FOUND, USER_DISABLED, USER_ENABLED, TOKEN_EXCEPTION, PERSISTENCE_ERROR, INVALID_DATA;
  }
}
