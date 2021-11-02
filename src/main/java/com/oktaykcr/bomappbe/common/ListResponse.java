package com.oktaykcr.bomappbe.common;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ListResponse<T> {
    private List<T> data;
    private Long totalCount;
    private Date date;

    public static <T> ListResponse<T> response(List<T> data, long totalCount) {
        ListResponse<T> listResponse = new ListResponse<T>();
        listResponse.setData(data);
        listResponse.setTotalCount(totalCount);
        listResponse.setDate(new Date());
        return listResponse;
    }
}
