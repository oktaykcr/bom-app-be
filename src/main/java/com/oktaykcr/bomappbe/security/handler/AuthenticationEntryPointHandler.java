package com.oktaykcr.bomappbe.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oktaykcr.bomappbe.exception.ApiExceptionResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ApiExceptionResponse apiErrorResponse = new ApiExceptionResponse();
        apiErrorResponse.setHttpStatus(ApiExceptionType.LOGIN_FAILURE.getHttpStatus());
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(ApiExceptionType.LOGIN_FAILURE.getErrorCode());

        httpServletResponse.setStatus(ApiExceptionType.LOGIN_FAILURE.getHttpStatus().value());
        OutputStream outputStream = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, apiErrorResponse);
        outputStream.flush();
    }
}
