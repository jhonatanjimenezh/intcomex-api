package com.intcomex.intcomex_api.config.exception;

public enum SPError {
    GENERIC_ERROR(1000, "General, uncontrolled errors"),
    APP_LOAD_ERROR(1001, "Errors during application loading"),
    DATABASE_CONNECTION_ERROR(1002, "Database connection failures"),
    INVALID_ARGUMENT_ERROR(1003, "Invalid argument provided to a method or function"),
    INVALID_REQUEST_ERROR(1004, "Malformed request such as unreadable or missing message body"),
    INVALID_PARAMS_ERROR(1005, "Malformed request with invalid data submitted"),
    DATABASE_ADAPTER_SAVE_ERROR(1007, "Errors saving records to the database"),
    DATABASE_ADAPTER_FIND_ERROR(1008, "Errors retrieving records from the database"),
    DATABASE_ADAPTER_UPDATE_ERROR(1009, "Errors updating records in the database"),
    DATABASE_ADAPTER_DELETE_ERROR(1010, "Errors deleting records from the database"),
    CONTROLLER_ERROR_CREATED(1011, "Controller error when creating"),
    CONTROLLER_ERROR_FIND_ALL(1012, "Controller error when finding all"),
    CONTROLLER_ERROR_FIND_BY_ID(1013, "Controller error when finding by ID"),
    CONTROLLER_ERROR_UPDATE(1014, "Controller error when updating"),
    CONTROLLER_ERROR_DELETE(1015, "Controller error when deleting");

    private final int errorCode;
    private final String errorMessage;

    SPError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
