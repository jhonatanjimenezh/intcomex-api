package com.intcomex.intcomex_api.adapter.controller.models;

import com.intcomex.intcomex_api.config.exception.SPError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse extends GenericResponse {

    public static ProductResponse of(Object result, HttpStatus httpStatus) {
        ProductResponse response = new ProductResponse();
        response.setStatus(true);
        response.setCodeStatus(httpStatus.value());
        response.setMessage(httpStatus.getReasonPhrase());
        response.setData(result);
        return response;
    }

    public static ProductResponse badRequest(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> String.format("Field: %s -> Error: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ProductResponse response = new ProductResponse();
        response.setStatus(false);
        response.setCodeStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(SPError.INVALID_PARAMS_ERROR.getErrorMessage());
        response.setData(errors);

        return response;
    }
}
