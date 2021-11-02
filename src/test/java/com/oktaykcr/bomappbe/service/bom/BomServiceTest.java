package com.oktaykcr.bomappbe.service.bom;

import com.oktaykcr.bomappbe.common.ListResponse;
import com.oktaykcr.bomappbe.exception.ApiException;
import com.oktaykcr.bomappbe.exception.ApiExceptionType;
import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.repository.bom.BomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BomServiceTest {

    @Autowired
    private BomService bomService;

    @MockBean
    private BomRepository bomRepository;

    private Bom createBom() {
        return new Bom("title", "description");
    }

    @Test
    public void list_shouldFindAll() {
        List<Bom> mockedBomList = Collections.singletonList(createBom());

        ListResponse<Bom> mockedListResponse = ListResponse.response(mockedBomList, mockedBomList.size());

        Page<Bom> pageBom = new PageImpl<>(mockedBomList);
        Pageable pageable = PageRequest.of(1, 1);
        Mockito.doReturn(pageBom).when(bomRepository).findAll(pageable);
        Mockito.doReturn((long) mockedBomList.size()).when(bomRepository).count();

        ListResponse<Bom> listResponse = bomService.list(1, 1);

        assertEquals(mockedListResponse.getTotalCount(), listResponse.getTotalCount());
        assertEquals(mockedListResponse.getData().get(0), listResponse.getData().get(0));
    }

    @Test
    public void list_pageNumberIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.list(-1, 1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageNumber";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void list_pageOffsetIsLessThanZero_shouldThrowException() {
        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.list(0, -1);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "pageOffset";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_shouldSaveBom() {
        Bom createdBom = createBom();
        createdBom.setId("bomId");

        Bom bomRequest = createBom();

        Mockito.doReturn(createdBom).when(bomRepository).save(Mockito.any());


        Bom result = bomService.save(bomRequest);

        assertEquals(createdBom.getId(), result.getId());
        assertEquals(createdBom.getDescription(), result.getDescription());
        assertEquals(createdBom.getTitle(), result.getTitle());
    }

    @Test
    public void save_titleIsBlank_shouldThrowException() {
        Bom bom = createBom();
        bom.setTitle(null);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.save(bom);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "title";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void save_descriptionIsBlank_shouldThrowException() {
        Bom bom = createBom();
        bom.setDescription(null);

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.save(bom);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "description";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_shouldUpdateBomTitle() {
        Bom updatedBom = createBom();
        updatedBom.setDescription(null);
        updatedBom.setId("bomId");

        Mockito.doReturn(Optional.of(updatedBom)).when(bomRepository).findById(updatedBom.getId());
        Mockito.doReturn(updatedBom).when(bomRepository).save(updatedBom);

        Bom result = bomService.update(updatedBom);

        assertEquals(updatedBom.getId(), result.getId());
        assertEquals(updatedBom.getDescription(), result.getDescription());
        assertEquals(updatedBom.getTitle(), result.getTitle());
    }

    @Test
    public void update_shouldUpdateBomDescription() {
        Bom updatedBom = createBom();
        updatedBom.setTitle(null);
        updatedBom.setId("bomId");

        Mockito.doReturn(Optional.of(updatedBom)).when(bomRepository).findById(updatedBom.getId());
        Mockito.doReturn(updatedBom).when(bomRepository).save(updatedBom);

        Bom result = bomService.update(updatedBom);

        assertEquals(updatedBom.getId(), result.getId());
        assertEquals(updatedBom.getDescription(), result.getDescription());
        assertEquals(updatedBom.getTitle(), result.getTitle());
    }

    @Test
    public void update_idIsBlank_shouldThrowException() {
        Bom bom = createBom();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.update(bom);
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void update_bomIsNotFound_shouldThrowException() {
        Bom bom = createBom();
        bom.setId("bomId");

        Mockito.doReturn(Optional.empty()).when(bomRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.update(bom);
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "bom";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_shouldFindBomById() {
        Bom bom = createBom();
        bom.setId("bomId");

        Mockito.doReturn(Optional.of(bom)).when(bomRepository).findById(bom.getId());

        Bom result = bomService.findById(bom.getId());

        assertEquals(bom.getId(), result.getId());
        assertEquals(bom.getDescription(), result.getDescription());
        assertEquals(bom.getTitle(), result.getTitle());
    }

    @Test
    public void findById_idIsBlank_shouldThrowException() {
        Bom bom = createBom();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.findById(bom.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void findById_bomIsNotFound_shouldThrowException() {
        Bom bom = createBom();
        bom.setId("bomId");

        Mockito.doReturn(Optional.empty()).when(bomRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.findById(bom.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "bom";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void deleteById_shouldDeleteBomById() {
        Bom bom = createBom();
        bom.setId("bomId");

        Mockito.doReturn(Optional.of(bom)).when(bomRepository).findById(bom.getId());

        Boolean result = bomService.deleteById(bom.getId());

        assertEquals(true, result);
    }

    @Test
    public void deleteById_idIsBlank_shouldThrowException() {
        Bom bom = createBom();

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.deleteById(bom.getId());
        });

        String expectedMessage = ApiExceptionType.BAD_REQUEST.getErrorCode();
        String expectedParam = "id";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }

    @Test
    public void deleteById_bomIsNotFound_shouldThrowException() {
        Bom bom = createBom();
        bom.setId("bomId");

        Mockito.doReturn(Optional.empty()).when(bomRepository).findById(Mockito.anyString());

        ApiException apiException = assertThrows(ApiException.class, () -> {
            bomService.deleteById(bom.getId());
        });

        String expectedMessage = ApiExceptionType.NOT_FOUND.getErrorCode();
        String expectedParam = "bom";

        assertEquals(expectedMessage, apiException.getMessage());
        assertEquals(apiException.getParams()[0], expectedParam);
    }


}