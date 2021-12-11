package com.oktaykcr.bomappbe.service.component;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.common.SecurityContextTestHelper;
import com.oktaykcr.bomappbe.common.TestDataFactory;
import com.oktaykcr.bomappbe.exception.ApiException;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.repository.bom.BomRepository;
import com.oktaykcr.bomappbe.repository.component.ComponentRepository;
import com.oktaykcr.bomappbe.repository.component.ComponentUsedRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ComponentUsedServiceTest {

    @Autowired
    private ComponentUsedService componentUsedService;

    @MockBean
    private ComponentUsedRepository componentUsedRepository;

    @MockBean
    private ComponentRepository componentRepository;

    @MockBean
    private BomRepository bomRepository;

    @Test
    public void save_shouldSaveComponentUsed() {
        ComponentUsed componentUsedRequest = TestDataFactory.createComponentUsed();
        componentUsedRequest.getBom().setId("bomId");

        Mockito.doReturn(Optional.of(componentUsedRequest.getBom())).when(bomRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(componentUsedRequest.getComponent())).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        Mockito.doReturn(componentUsedRequest.getComponent()).when(componentRepository).save(Mockito.any(Component.class));
        Mockito.doReturn(componentUsedRequest).when(componentUsedRepository).save(Mockito.any(ComponentUsed.class));

        ComponentUsed result = componentUsedService.save(componentUsedRequest);

        assertEquals(componentUsedRequest.getCost(), result.getCost());
        assertEquals(componentUsedRequest.getQuantity(), result.getQuantity());
        assertEquals(componentUsedRequest.getLeadTime(), result.getLeadTime());
    }

    @Test
    public void save_bomIdIsNull_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "bomId";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_componentMouserPartNumberIsNull_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");
        componentUsed.getComponent().setMouserPartNumber(null);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "mouserPartNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_quantityIsLessThanZero_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");
        componentUsed.setQuantity(-1);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "quantity";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_bomIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");

        Mockito.doReturn(Optional.empty()).when(bomRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "bom";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_componentIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");

        Mockito.doReturn(Optional.of(componentUsed.getBom())).when(bomRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.empty()).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_quantityOnHandLessThanQuantity_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");

        componentUsed.getComponent().setQuantityOnHand(500);
        componentUsed.setQuantity(1000);

        Mockito.doReturn(Optional.of(componentUsed.getBom())).when(bomRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(componentUsed.getComponent())).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.CONFLICT.getErrorCode();
        String expectedParam = "quantityOnHand";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_BomIdIsSameAndComponentPartNumberIsNotUnique_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getBom().setId("bomId");

        componentUsed.getComponent().setQuantityOnHand(500);
        componentUsed.setQuantity(1000);

        Mockito.doReturn(Optional.of(componentUsed.getBom())).when(bomRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(componentUsed.getComponent())).when(componentRepository).findByMouserPartNumber(Mockito.anyString());
        Mockito.doReturn(List.of(componentUsed)).when(componentUsedRepository).findByBomIdAndComponentMouserPartNumber(Mockito.anyString(), Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.save(componentUsed);
        });

        String expectedMessage = ApiExceptionType.CONFLICT.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_shouldUpdateComponentUsedAndComponentQuantityOnHand() {
        ComponentUsed oldComponentUsed = TestDataFactory.createComponentUsed();
        oldComponentUsed.setId("componentUsedId");

        oldComponentUsed.setQuantity(10);
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.getComponent().setQuantityOnHand(5);
        componentUsed.setCost(5);
        componentUsed.setQuantity(5);
        componentUsed.setLeadTime(5);
        componentUsed.setId("componentUsedId");

        Component component = TestDataFactory.createComponent();
        component.setId("componentId");
        component.setQuantityOnHand(10);

        Mockito.doReturn(Optional.of(oldComponentUsed)).when(componentUsedRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(componentUsed.getComponent())).when(componentRepository).findByMouserPartNumber(Mockito.anyString());
        Mockito.doReturn(component).when(componentRepository).save(Mockito.any(Component.class));
        Mockito.doReturn(componentUsed).when(componentUsedRepository).save(Mockito.any(ComponentUsed.class));

        ComponentUsed result = componentUsedService.update(componentUsed);

        assertEquals(componentUsed.getId(), result.getId());
        assertEquals(componentUsed.getCost(), result.getCost());
        assertEquals(componentUsed.getQuantity(), result.getQuantity());
        assertEquals(componentUsed.getLeadTime(), result.getLeadTime());
        assertEquals(componentUsed.getExtendedCost(), result.getExtendedCost());
        assertEquals(component.getMouserPartNumber(), result.getComponent().getMouserPartNumber());
        assertEquals(component.getQuantityOnHand(), result.getComponent().getQuantityOnHand());
        assertEquals(componentUsed.getBom().getId(), result.getBom().getId());
    }

    @Test
    public void update_idIsBlank_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.update(componentUsed);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_componentUsedIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Mockito.doReturn(Optional.empty()).when(componentUsedRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.update(componentUsed);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "componentUsed";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_componentIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Mockito.doReturn(Optional.of(componentUsed)).when(componentUsedRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.empty()).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.update(componentUsed);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "component";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_componentQuantityOnHandLessThanComponentUsedQuantity_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");
        componentUsed.getComponent().setQuantityOnHand(5);
        componentUsed.setQuantity(10);

        Mockito.doReturn(Optional.of(componentUsed)).when(componentUsedRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(componentUsed.getComponent())).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.update(componentUsed);
        });

        String expectedMessage = ApiExceptionType.CONFLICT.getErrorCode();
        String expectedParam = "quantityOnHand";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listPaginated_shouldFindAll() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        List<ComponentUsed> mockedComponentUsedList = Collections.singletonList(componentUsed);
        ListResponse<ComponentUsed> mockedListResponse = ListResponse.response(mockedComponentUsedList, mockedComponentUsedList.size());

        SecurityContextTestHelper.mockSecurityContextHolder();
        Page<ComponentUsed> pageComponentUsed = new PageImpl<>(mockedComponentUsedList);
        Mockito.doReturn(pageComponentUsed).when(componentUsedRepository).findAllByBomUserUsername(Mockito.anyString(), Mockito.any(Pageable.class));
        Mockito.doReturn((long) mockedComponentUsedList.size()).when(componentUsedRepository).countComponentByBomUserUsername(Mockito.anyString());

        ListResponse<ComponentUsed> listResponse = componentUsedService.listPaginated(1, 1);

        assertEquals(mockedListResponse.getTotalCount(), listResponse.getTotalCount());
        assertEquals(mockedListResponse.getData().get(0), listResponse.getData().get(0));
    }

    @Test
    public void listPaginated_pageNumberIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.listPaginated(-1, 1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listPaginated_pageOffsetIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.listPaginated(0, -1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageOffset";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void listAllByBomId_shouldFindAllComponentsUsed() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();

        List<ComponentUsed> mockedComponentUsedList = new ArrayList<>();
        mockedComponentUsedList.add(componentUsed);

        ListResponse<ComponentUsed> mockedListResponse = ListResponse.response(mockedComponentUsedList, mockedComponentUsedList.size());

        SecurityContextTestHelper.mockSecurityContextHolder();

        Mockito.doReturn(mockedComponentUsedList).when(componentUsedRepository).findAllByBomUserUsernameAndBomId(Mockito.anyString(), Mockito.anyString());

        ListResponse<ComponentUsed> result = componentUsedService.listAllByBomId("bomId");

        assertEquals(mockedListResponse.getTotalCount(), result.getTotalCount());
        assertEquals(mockedListResponse.getData().get(0), result.getData().get(0));
    }

    @Test
    public void listAllByBomId_bomIdIsNull_shouldThrowException() {
        SecurityContextTestHelper.mockSecurityContextHolder();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.listAllByBomId(null);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "bomId";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_shouldFindComponentUsedById() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Mockito.doReturn(Optional.of(componentUsed)).when(componentUsedRepository).findById(componentUsed.getId());

        ComponentUsed result = componentUsedService.findById(componentUsed.getId());

        assertEquals(componentUsed.getId(), result.getId());
        assertEquals(componentUsed.getCost(), result.getCost());
        assertEquals(componentUsed.getQuantity(), result.getQuantity());
        assertEquals(componentUsed.getExtendedCost(), result.getExtendedCost());
        assertEquals(componentUsed.getLeadTime(), result.getLeadTime());
        assertEquals(componentUsed.getBom().getId(), result.getBom().getId());
        assertEquals(componentUsed.getComponent().getMouserPartNumber(), result.getComponent().getMouserPartNumber());
    }

    @Test
    public void findById_idIsBlank_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.findById(componentUsed.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_componentUsedIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Mockito.doReturn(Optional.empty()).when(componentUsedRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.findById(componentUsed.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "componentUsed";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void deleteById_shouldDeleteComponentUsedById() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Component component = TestDataFactory.createComponent();

        Mockito.doReturn(Optional.of(componentUsed)).when(componentUsedRepository).findById(Mockito.anyString());
        Mockito.doReturn(Optional.of(component)).when(componentRepository).findByMouserPartNumber(Mockito.anyString());

        component.setQuantityOnHand(component.getQuantityOnHand() + componentUsed.getQuantity());
        Mockito.doReturn(component).when(componentRepository).save(Mockito.any());

        Boolean result = componentUsedService.deleteById(componentUsed.getId());

        assertEquals(true, result);
    }

    @Test
    public void deleteById_idIsBlank_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.deleteById(componentUsed.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void deleteById_componentUsedIsNotFound_shouldThrowException() {
        ComponentUsed componentUsed = TestDataFactory.createComponentUsed();
        componentUsed.setId("componentUsedId");

        Mockito.doReturn(Optional.empty()).when(componentUsedRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            componentUsedService.deleteById(componentUsed.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "componentUsed";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }


}
