package com.oktaykcr.bomappbe.service.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.common.SecurityContextTestHelper;
import com.oktaykcr.bomappbe.common.TestDataFactory;
import com.oktaykcr.bomappbe.exception.ApiException;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.component.ComponentRepository;
import com.oktaykcr.bomappbe.repository.inventory.InventoryRepository;
import com.oktaykcr.bomappbe.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ComponentServiceTest {

    @Autowired
    private ComponentService componentService;

    @MockBean
    private ComponentRepository componentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void deleteById_shouldDeleteComponentById() {
        Component component = TestDataFactory.createComponent();
        component.setId("comId");

        Mockito.doReturn(Optional.of(component)).when(componentRepository).findById(component.getId());

        Boolean result = componentService.deleteById(component.getId());

        assertEquals(true, result);
    }

    @Test
    public void deleteById_idIsBlank_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.deleteById(component.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void deleteById_componentIsNotFound_shouldThrowException() {
        Component component = TestDataFactory.createComponent();
        component.setId("comId");

        Mockito.doReturn(Optional.empty()).when(componentRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.deleteById(component.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_shouldFindComponentById() {
        Component component = TestDataFactory.createComponent();
        component.setId("comId");

        Mockito.doReturn(Optional.of(component)).when(componentRepository).findById(component.getId());

        Component result = componentService.findById(component.getId());

        assertEquals(component.getId(), result.getId());
        assertEquals(component.getDescription(), result.getDescription());
        assertEquals(component.getSupplierLink(), result.getSupplierLink());
        assertEquals(component.getMouserPartNumber(), result.getMouserPartNumber());
        assertEquals(component.getQuantityOnHand(), result.getQuantityOnHand());
        assertEquals(component.getImageUrl(), result.getImageUrl());
        assertEquals(component.getManufacturerName(), result.getManufacturerName());
    }

    @Test
    public void findById_idIsBlank_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.findById(component.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_componentIsNotFound_shouldThrowException() {
        Component component = TestDataFactory.createComponent();
        component.setId("bomId");

        Mockito.doReturn(Optional.empty()).when(componentRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.findById(component.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listPaginated_shouldFindAll() {
        Component component = TestDataFactory.createComponent();
        List<Component> mockedComponentList = Collections.singletonList(component);
        ListResponse<Component> mockedListResponse = ListResponse.response(mockedComponentList, mockedComponentList.size());

        SecurityContextTestHelper.mockSecurityContextHolder();
        Page<Component> pageBom = new PageImpl<>(mockedComponentList);
        Mockito.doReturn(pageBom).when(componentRepository).findAllByInventoryUserUsername(Mockito.anyString(), Mockito.any(Pageable.class));
        Mockito.doReturn((long) mockedComponentList.size()).when(componentRepository).countComponentByInventoryUserUsername(Mockito.anyString());

        ListResponse<Component> listResponse = componentService.listPaginated(1, 1);

        assertEquals(mockedListResponse.getTotalCount(), listResponse.getTotalCount());
        assertEquals(mockedListResponse.getData().get(0), listResponse.getData().get(0));
    }

    @Test
    public void listPaginated_pageNumberIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.listPaginated(-1, 1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listPaginated_pageOffsetIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.listPaginated(0, -1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageOffset";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listAll_shouldFindAllComponents() {
        Component component = TestDataFactory.createComponent();
        List<Component> mockedComponentList = new ArrayList<>();
        mockedComponentList.add(component);

        SecurityContextTestHelper.mockSecurityContextHolder();

        ListResponse<Component> mockedListResponse = ListResponse.response(mockedComponentList, mockedComponentList.size());

        Mockito.doReturn(mockedComponentList).when(componentRepository).findAllByInventoryUserUsername(Mockito.anyString());

        ListResponse<Component> result = componentService.listAll();

        assertEquals(mockedListResponse.getTotalCount(), result.getTotalCount());
        assertEquals(mockedListResponse.getData().get(0), result.getData().get(0));
    }

    @Test
    public void update_shouldUpdateComponentSupplierLink() {
        Component component = TestDataFactory.createComponent();
        component.setId("comId");

        Mockito.doReturn(Optional.of(component)).when(componentRepository).findById(component.getId());
        Mockito.doReturn(component).when(componentRepository).save(component);

        Component result = componentService.update(component);

        assertEquals(component.getId(), result.getId());
        assertEquals(component.getSupplierLink(), result.getSupplierLink());
    }

    @Test
    public void update_idIsBlank_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.update(component);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_componentIsNotFound_shouldThrowException() {
        Component component = TestDataFactory.createComponent();
        component.setId("comId");

        Mockito.doReturn(Optional.empty()).when(componentRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.update(component);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_shouldSaveComponent() {
        User user = TestDataFactory.createUser();
        Component component = TestDataFactory.createComponent();
        component.setImageUrl("https://www.mouser.com/images/vishay/images/TNPU_DSL.jpg");
        component.setManufacturerName("Vishay / Draloric");
        component.setDescription("Thin Film Resistors - SMD 1/10W 7.5Kohms 0.1% 5ppm");

        Inventory inventory = TestDataFactory.createInventory();
        inventory.setUser(user);

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
        Mockito.doReturn(Optional.of(inventory)).when(inventoryRepository).getByUserUsername(Mockito.any());
        Mockito.doReturn(component).when(componentRepository).save(Mockito.any());

        try {
            ObjectNode jsonData = (ObjectNode) new ObjectMapper().readTree(TestDataFactory.createMouserData());
            ResponseEntity<ObjectNode> responseEntity = new ResponseEntity<>(jsonData, HttpStatus.ACCEPTED);
            Mockito.when(restTemplate.exchange(
                    ArgumentMatchers.any(),
                    ArgumentMatchers.any(HttpMethod.class),
                    ArgumentMatchers.any(),
                    ArgumentMatchers.<Class<ObjectNode>>any()
                    )
            ).thenReturn(responseEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Component componentRequest = new Component();
        componentRequest.setMouserPartNumber(component.getMouserPartNumber());
        componentRequest.setSupplierLink(component.getSupplierLink());
        componentRequest.setQuantityOnHand(component.getQuantityOnHand());

        Component result = componentService.save(componentRequest);

        assertEquals(component.getMouserPartNumber(), result.getMouserPartNumber());
        assertEquals(component.getSupplierLink(), result.getSupplierLink());
        assertEquals(component.getQuantityOnHand(), result.getQuantityOnHand());
        assertEquals(component.getImageUrl(), result.getImageUrl());
        assertEquals(component.getManufacturerName(), result.getManufacturerName());
        assertEquals(component.getDescription(), result.getDescription());
        assertEquals(component.getManufacturerPartNumber(), result.getManufacturerPartNumber());
        assertEquals(component.getDataSheetUrl(), result.getDataSheetUrl());
    }

    @Test
    public void save_userIsNotFound_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        SecurityContextTestHelper.mockSecurityContextHolder();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "user";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_mouserPartNumberIsNull_shouldThrowException() {
        Component component = TestDataFactory.createComponent();
        component.setMouserPartNumber(null);

        User user = TestDataFactory.createUser();

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "mouserPartNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_componentAlreadySaved_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        User user = TestDataFactory.createUser();

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
        Mockito.doReturn(Optional.of(component)).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.CONFLICT.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_supplierLinkIsNull_shouldThrowException() {
        Component component = TestDataFactory.createComponent();
        component.setSupplierLink(null);

        User user = TestDataFactory.createUser();

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "supplierLink";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_mouserNumberOfResultsLessThanZero_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        User user = TestDataFactory.createUser();

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());

        try {
            ObjectNode jsonData = (ObjectNode) new ObjectMapper().readTree(TestDataFactory.createEmptyMouserData());
            ResponseEntity<ObjectNode> responseEntity = new ResponseEntity<>(jsonData, HttpStatus.ACCEPTED);
            Mockito.when(restTemplate.exchange(
                    ArgumentMatchers.any(),
                    ArgumentMatchers.any(HttpMethod.class),
                    ArgumentMatchers.any(),
                    ArgumentMatchers.<Class<ObjectNode>>any()
                    )
            ).thenReturn(responseEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "partNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_userInventoryNotFound_shouldThrowException() {
        Component component = TestDataFactory.createComponent();

        User user = TestDataFactory.createUser();

        SecurityContextTestHelper.mockSecurityContextHolder();
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());

        try {
            ObjectNode jsonData = (ObjectNode) new ObjectMapper().readTree(TestDataFactory.createMouserData());
            ResponseEntity<ObjectNode> responseEntity = new ResponseEntity<>(jsonData, HttpStatus.ACCEPTED);
            Mockito.when(restTemplate.exchange(
                    ArgumentMatchers.any(),
                    ArgumentMatchers.any(HttpMethod.class),
                    ArgumentMatchers.any(),
                    ArgumentMatchers.<Class<ObjectNode>>any()
                    )
            ).thenReturn(responseEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentService.save(component);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "inventory";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

}
