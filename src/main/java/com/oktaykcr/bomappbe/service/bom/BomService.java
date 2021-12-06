package com.oktaykcr.bomappbe.service.bom;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.bom.BomRepository;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import com.oktaykcr.bomappbe.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BomService extends BaseService<Bom> {

    private final BomRepository bomRepository;
    private final UserRepository userRepository;

    @Autowired
    public BomService(BomRepository bomRepository, UserRepository userRepository) {
        this.bomRepository = bomRepository;
        this.userRepository = userRepository;
    }

    public ListResponse<Bom> listPaginated(Integer pageNumber, Integer pageOffset) {
        Pageable pageable = createPageable(pageNumber, pageOffset);
        String username = getCurrentAuthenticatedUsername();
        Page<Bom> pagedBom = bomRepository.findAllByUserUsername(username, pageable);
        long totalCount = bomRepository.countBomByUserUsername(username);

        return ListResponse.response(pagedBom.getContent(), totalCount);
    }

    public ListResponse<Bom> searchByTitle(String title) {
        List<Bom> boms;
        String username = getCurrentAuthenticatedUsername();
        if(!StringUtils.isBlank(title)) {
            boms = bomRepository.findAllByUserUsernameAndTitleContainingIgnoreCase(username, title);
            return ListResponse.response(boms, boms.size());
        }

        boms = bomRepository.findAllByUserUsername(username);

        return ListResponse.response(boms, boms.size());
    }

    public Bom save(Bom bom) {
        User user = userRepository.findByUsername(getCurrentAuthenticatedUsername());
        if(user == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "user");
        }

        if(StringUtils.isBlank(bom.getTitle())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "title");
        }

        if(StringUtils.isBlank(bom.getDescription())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "description");
        }

        Optional<Bom> foundBom = bomRepository.findByUserUsernameAndTitle(user.getUsername(), bom.getTitle());
        if(foundBom.isPresent()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.CONFLICT, "title");
        }

        bom.setUser(user);

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
