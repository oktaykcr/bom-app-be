package com.oktaykcr.bomappbe.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ApiExceptionResponse {

    private Date timestamp;
    private String errorCode;
    private HttpStatus httpStatus;
    private String[] params;
}
