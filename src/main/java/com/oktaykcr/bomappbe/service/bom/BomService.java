package com.oktaykcr.bomappbe.service.bom;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.repository.bom.BomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class BomService {

    private final BomRepository bomRepository;

    @Autowired
    public BomService(BomRepository bomRepository) {
        this.bomRepository = bomRepository;
    }

    public ListResponse<Bom> list(Integer pageNumber, Integer pageOffset) {
        if(pageNumber < 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "pageNumber");
        }

        if(pageOffset <= 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "pageOffset");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageOffset);

        Page<Bom> pagedBom = bomRepository.findAll(pageable);
        long totalCount = bomRepository.count();

        return ListResponse.response(pagedBom.getContent(), totalCount);
    }

    public Bom save(Bom bom) {
        if(StringUtils.isBlank(bom.getTitle())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "title");
        }

        if(StringUtils.isBlank(bom.getDescription())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "description");
        }

        return bomRepository.save(bom);
    }

    public Bom update(Bom bom) {
        if(StringUtils.isBlank(bom.getId())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Bom> foundBom = bomRepository.findById(bom.getId());
        if(foundBom.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "bom");
        }

        if(!StringUtils.isBlank(bom.getTitle())) {
            foundBom.get().setTitle(bom.getTitle());
        }

        if(!StringUtils.isBlank(bom.getDescription())) {
            foundBom.get().setDescription(bom.getDescription());
        }

        foundBom.get().setUpdatedDate(Date.from(Instant.now()));

        return bomRepository.save(foundBom.get());
    }

    public Bom findById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Bom> foundBom = bomRepository.findById(id);
        if(foundBom.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "bom");
        }

        return foundBom.get();
    }

    public Boolean deleteById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Bom> foundBom = bomRepository.findById(id);
        if(foundBom.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "bom");
        }

        bomRepository.deleteById(id);

        return true;
    }
}
