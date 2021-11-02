package com.oktaykcr.bomappbe.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public final ResponseEntity<ApiExceptionResponse> handleApiExceptions(ApiException apiException) {
        ApiExceptionResponse apiErrorResponse = new ApiExceptionResponse();
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(apiException.getMessage());
        apiErrorResponse.setHttpStatus(apiException.getHttpStatus());
        apiErrorResponse.setParams(apiException.getParams());
        return new ResponseEntity<>(apiErrorResponse, apiException.getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiExceptionResponse> handleOtherExcepitons(Exception ex) {
        ApiExceptionResponse apiErrorResponse = new ApiExceptionResponse();
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(ApiExceptionType.INTERNAL_SERVER_ERROR.getErrorCode());
        apiErrorResponse.setHttpStatus(ApiExceptionType.INTERNAL_SERVER_ERROR.getHttpStatus());

        return new ResponseEntity<>(apiErrorResponse, ApiExceptionType.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
