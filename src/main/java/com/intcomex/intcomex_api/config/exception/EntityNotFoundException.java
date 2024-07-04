package com.intcomex.intcomex_api.config.exception;

public class EntityNotFoundException extends GenericException {
    public EntityNotFoundException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
