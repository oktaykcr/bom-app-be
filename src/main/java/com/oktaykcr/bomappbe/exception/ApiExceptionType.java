package com.oktaykcr.bomappbe.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public enum ApiExceptionType {

    INTERNAL_SERVER_ERROR("API_001",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("API_002", HttpStatus.BAD_REQUEST),
    NOT_FOUND("API_003", HttpStatus.NOT_FOUND),
    CONFLICT("API_004", HttpStatus.CONFLICT),
    LOGIN_FAILURE("API_005", HttpStatus.UNAUTHORIZED),
    FILE_STORAGE("API_006", HttpStatus.INTERNAL_SERVER_ERROR);

    ApiExceptionType(String errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    @Getter @Setter
    private String errorCode;

    @Getter @Setter
    private HttpStatus httpStatus;
}
