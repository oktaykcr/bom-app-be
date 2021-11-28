package com.oktaykcr.bomappbe.service.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiExceptionFactory;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.component.ComponentRepository;
import com.oktaykcr.bomappbe.repository.inventory.InventoryRepository;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import com.oktaykcr.bomappbe.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentService extends BaseService<Component> {

    private final ComponentRepository componentRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    private final RestTemplate restTemplate;

    @Value("${mouser.api.search.url}")
    private String mouserApiUrl;

    @Value("${mouser.api.search.key}")
    private String mouserApiKey;

    @Value("${mouser.api.search.key.header}")
    private String mouserApiKeyHeader;

    @Autowired
    public ComponentService(ComponentRepository componentRepository, UserRepository userRepository, InventoryRepository inventoryRepository, RestTemplate restTemplate) {
        this.componentRepository = componentRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Component save(Component component) {
        User user = userRepository.findByUsername(getCurrentAuthenticatedUsername());
        if(user == null) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "user");
        }

        if(StringUtils.isBlank(component.getPartNumber())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "partNumber");
        }

        Optional<Component> foundComponent = componentRepository.findByPartNumber(component.getPartNumber());
        if(foundComponent.isPresent()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.CONFLICT, "component");
        }

        if(StringUtils.isBlank(component.getSupplierLink())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "supplierLink");
        }

        ResponseEntity<ObjectNode> response = getMouserComponentByPartNumber(component);
        JsonNode searchResults = response.getBody().get("SearchResults");

        if(searchResults.get("NumberOfResult").intValue() <= 0) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "partNumber");
        }

        JsonNode firstItem = searchResults.get("Parts").get(0);
        String description = firstItem.get("Description").textValue();
        String imagePath = firstItem.get("ImagePath").textValue();
        String manufacturer = firstItem.get("Manufacturer").textValue();

        component.setDescription(description);
        component.setImageUrl(imagePath);
        component.setManufacturerName(manufacturer);

        Optional<Inventory> currentUserInventory = inventoryRepository.getByUserUsername(user.getUsername());
        if(currentUserInventory.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "inventory");
        }

        component.setInventory(currentUserInventory.get());

        return componentRepository.save(component);
    }

    private ResponseEntity<ObjectNode> getMouserComponentByPartNumber(Component component) {
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromHttpUrl(mouserApiUrl).queryParam(mouserApiKeyHeader, mouserApiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        ObjectNode requestNode = createObjectNodeForSearchRequest(component);

        HttpEntity<ObjectNode> entity = new HttpEntity<>(requestNode, headers);

        return restTemplate.exchange(uriComponentsBuilder.build().encode().toUri(), HttpMethod.POST, entity, ObjectNode.class);
    }

    private ObjectNode createObjectNodeForSearchRequest(Component component) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode childNode = mapper.createObjectNode();
        childNode.put("mouserPartNumber", component.getPartNumber());

        rootNode.set("SearchByPartRequest", childNode);
        return rootNode;
    }

    @Override
    public Component update(Component component) {
        if(StringUtils.isBlank(component.getId())) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Component> foundComponent = componentRepository.findById(component.getId());
        if(foundComponent.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "component");
        }

        if(!StringUtils.isBlank(component.getSupplierLink())) {
            foundComponent.get().setSupplierLink(component.getSupplierLink());
        }

        foundComponent.get().setQuantityOnHand(component.getQuantityOnHand());

        return componentRepository.save(foundComponent.get());
    }

    @Override
    public ListResponse<Component> listPaginated(Integer pageNumber, Integer pageOffset) {
        Pageable pageable = createPageable(pageNumber, pageOffset);

        String username = getCurrentAuthenticatedUsername();
        Page<Component> pagedComponent = componentRepository.findAllByInventoryUserUsername(username, pageable);
        long totalCount = componentRepository.countComponentByInventoryUserUsername(username);

        return ListResponse.response(pagedComponent.getContent(), totalCount);
    }

    public ListResponse<Component> listAll() {
        String username = getCurrentAuthenticatedUsername();
        List<Component> components = componentRepository.findAllByInventoryUserUsername(username);
        return ListResponse.response(components, components.size());
    }

    @Override
    public Component findById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Component> foundComponent = componentRepository.findById(id);
        if(foundComponent.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "component");
        }

        return foundComponent.get();
    }

    @Override
    public Boolean deleteById(String id) {
        if(StringUtils.isBlank(id)) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.BAD_REQUEST, "id");
        }

        Optional<Component> foundComponent = componentRepository.findById(id);
        if(foundComponent.isEmpty()) {
            throw ApiExceptionFactory.getApiException(ApiExceptionType.NOT_FOUND, "component");
        }

        componentRepository.deleteById(id);

        return true;
    }
}
