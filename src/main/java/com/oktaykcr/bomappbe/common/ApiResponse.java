package com.oktaykcr.bomappbe.common;

import lombok.Data;

import java.util.Date;

@Data
public class ApiResponse<T> {

    private T data;
    private Date date;

    public static <T> ApiResponse<T> response(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setData(data);
        apiResponse.setDate(new Date());
        return apiResponse;
    }
}
