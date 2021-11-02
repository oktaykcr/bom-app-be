package com.oktaykcr.bomappbe.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends RuntimeException {

    private String[] params;
    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, HttpStatus httpStatus, String ...params) {
        super(message);
        this.httpStatus = httpStatus;
        this.params = params;
    }
}
