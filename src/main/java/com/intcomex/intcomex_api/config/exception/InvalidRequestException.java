package com.intcomex.intcomex_api.config.exception;

public class InvalidRequestException extends GenericException {
    public InvalidRequestException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
