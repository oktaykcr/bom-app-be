package com.oktaykcr.bomappbe.service.component;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.repository.bom.BomRepository;
import com.oktaykcr.bomappbe.repository.component.ComponentRepository;
import com.oktaykcr.bomappbe.repository.component.ComponentUsedRepository;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import com.oktaykcr.bomappbe.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentUsedService extends BaseService<ComponentUsed> {

    private final ComponentUsedRepository componentUsedRepository;
    private final ComponentRepository componentRepository;
    private final BomRepository bomRepository;

    @Autowired
    public ComponentUsedService(ComponentUsedRepository componentUsedRepository, ComponentRepository componentRepository, BomRepository bomRepository, UserRepository userRepository) {
        this.componentUsedRepository = componentUsedRepository;
        this.componentRepository = componentRepository;
        this.bomRepository = bomRepository;
    }

    @Override
    @Transactional
    public ComponentUsed save(ComponentUsed componentUsed) {
        if(StringUtils.isBlank(componentUsed.getBom().getId())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "bomId");
        }

        if(StringUtils.isBlank(componentUsed.getComponent().getPartNumber())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "partNumber");
        }

        if(componentUsed.getQuantity() <= 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "quantity");
        }

        List<ComponentUsed> foundComponentUsed = componentUsedRepository
                .findByBomIdAndComponentPartNumber(componentUsed.getBom().getId(), componentUsed.getComponent().getPartNumber());
        if(foundComponentUsed.size() > 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.CONFLICT, "component");
        }

        Bom foundBom = getBomByIdIfExists(componentUsed.getBom().getId());
        Component foundComponent = getComponentByComponentUsedIfExists(componentUsed);

        if(foundComponent.getQuantityOnHand() - componentUsed.getQuantity() < 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.CONFLICT, "quantityOnHand");
        }

        int remain = foundComponent.getQuantityOnHand() - componentUsed.getQuantity();
        foundComponent.setQuantityOnHand(remain);

        Component resultComponent = componentRepository.save(foundComponent);

        componentUsed.setComponent(resultComponent);
        componentUsed.setBom(foundBom);

        return componentUsedRepository.save(componentUsed);
    }

    @Override
    @Transactional
    public ComponentUsed update(ComponentUsed componentUsed) {
        if(StringUtils.isBlank(componentUsed.getId())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        ComponentUsed foundComponentUsed = getComponentUsedUsedByIdIfExists(componentUsed.getId());
        Component foundComponent = getComponentByComponentUsedIfExists(componentUsed);

        if(foundComponent.getQuantityOnHand() - componentUsed.getQuantity() < 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.CONFLICT, "quantityOnHand");
        }

        int totalAvailableQuantity = foundComponent.getQuantityOnHand() + foundComponentUsed.getQuantity();
        int remain = totalAvailableQuantity - componentUsed.getQuantity();

        foundComponent.setQuantityOnHand(remain);
        Component savedComponent = componentRepository.save(foundComponent);
        foundComponentUsed.setComponent(savedComponent);

        foundComponentUsed.setQuantity(componentUsed.getQuantity());
        foundComponentUsed.setCost(componentUsed.getCost());
        foundComponentUsed.setLeadTime(componentUsed.getLeadTime());

        return componentUsedRepository.save(foundComponentUsed);
    }

    @Override
    public ListResponse<ComponentUsed> listPaginated(Integer pageNumber, Integer pageOffset) {
        Pageable pageable = createPageable(pageNumber, pageOffset);

        String username = getCurrentAuthenticatedUsername();
        Page<ComponentUsed> pagedComponentUsed = componentUsedRepository.findAllByBomUserUsername(username, pageable);
        long totalCount = componentUsedRepository.countComponentByBomUserUsername(username);

        return ListResponse.response(pagedComponentUsed.getContent(), totalCount);
    }

    public ListResponse<ComponentUsed> listAllByBomId(String bomId) {
        String username = getCurrentAuthenticatedUsername();

        if(StringUtils.isBlank(bomId)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "bomId");
        }

        List<ComponentUsed> componentsUsed = componentUsedRepository.findAllByBomUserUsernameAndBomId(username, bomId);
        return ListResponse.response(componentsUsed, componentsUsed.size());
    }

    @Override
    public ComponentUsed findById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<ComponentUsed> foundComponentUsed = componentUsedRepository.findById(id);
        if(foundComponentUsed.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "componentUsed");
        }

        return foundComponentUsed.get();
    }

    @Override
    @Transactional
    public Boolean deleteById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        ComponentUsed foundComponentUsed = getComponentUsedUsedByIdIfExists(id);
        Component foundComponent = getComponentByComponentUsedIfExists(foundComponentUsed);

        int totalAvailableQuantity = foundComponent.getQuantityOnHand() + foundComponentUsed.getQuantity();
        foundComponent.setQuantityOnHand(totalAvailableQuantity);

        componentRepository.save(foundComponent);

        componentUsedRepository.deleteById(id);

        return true;
    }

    private Component getComponentByComponentUsedIfExists(ComponentUsed componentUsed) {
        Optional<Component> foundComponent = componentRepository.findByPartNumber(componentUsed.getComponent().getPartNumber());
        if (foundComponent.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "component");
        }
        return foundComponent.get();
    }

    private ComponentUsed getComponentUsedUsedByIdIfExists(String id) {
        Optional<ComponentUsed> foundComponentUsed = componentUsedRepository.findById(id);
        if(foundComponentUsed.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "componentUsed");
        }
        return foundComponentUsed.get();
    }

    private Bom getBomByIdIfExists(String id) {
        Optional<Bom> foundBom = bomRepository.findById(id);
        if(foundBom.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "bom");
        }
        return foundBom.get();
    }
}
