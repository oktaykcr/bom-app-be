package com.oktaykcr.bomappbe.service.base;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.base.BaseModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService<T extends BaseModel> {

    public abstract T save(T t);
    public abstract T update(T t);
    public abstract ListResponse<T> listPaginated(Integer pageNumber, Integer pageOffset);
    public abstract T findById(String id);
    public abstract Boolean deleteById(String id);

    protected Pageable createPageable(Integer pageNumber, Integer pageOffset) {
        if(pageNumber < 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "pageNumber");
        }

        if(pageOffset <= 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "pageOffset");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageOffset);
        return pageable;
    }

    protected String getCurrentAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.LOGIN_FAILURE);
        }
        return authentication.getName();
    }
}
