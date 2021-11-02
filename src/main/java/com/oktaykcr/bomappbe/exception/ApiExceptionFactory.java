package com.oktaykcr.bomappbe.exception;

public class ApiExceptionFactory {

    private ApiExceptionFactory() {}

    public static ApiException getApiException(ApiExceptionType apiException, String ...params) {
        return new ApiException(apiException.getErrorCode(), apiException.getHttpStatus(), params);
    }
}
