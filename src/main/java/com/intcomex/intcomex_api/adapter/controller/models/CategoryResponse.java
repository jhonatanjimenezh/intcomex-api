package com.intcomex.intcomex_api.adapter.controller.models;

import com.intcomex.intcomex_api.config.exception.SPError;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponse extends GenericResponse {

    public static CategoryResponse of(Object result, HttpStatus httpStatus) {
        CategoryResponse response = new CategoryResponse();
        response.setStatus(true);
        response.setCodeStatus(httpStatus.value());
        response.setMessage(httpStatus.getReasonPhrase());
        response.setData(result);
        return response;
    }

    public static CategoryResponse badRequest(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> String.format("Field: %s -> Error: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        CategoryResponse response = new CategoryResponse();
        response.setStatus(false);
        response.setCodeStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(SPError.INVALID_PARAMS_ERROR.getErrorMessage());
        response.setData(errors);

        return response;
    }
}
