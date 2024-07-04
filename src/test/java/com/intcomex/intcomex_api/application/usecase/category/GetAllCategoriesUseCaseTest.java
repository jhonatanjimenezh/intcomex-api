package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllCategoriesUseCaseTest {

    @Mock
    private CategoryStorageRepository repository;

    @InjectMocks
    private GetAllCategoriesUseCase useCase;

    private CategoryDomain categoryDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryDomain = new CategoryDomain();
        categoryDomain.setId(1L);
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");
    }

    @Test
    void getAll_shouldReturnListOfCategories_whenSuccessful() {
        when(repository.getAll()).thenReturn(Arrays.asList(categoryDomain));

        List<CategoryDomain> result = useCase.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDomain.getId(), result.get(0).getId());
        verify(repository, times(1)).getAll();
    }

    @Test
    void getAll_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(repository.getAll()).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getAll());

        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(repository, times(1)).getAll();
    }

    @Test
    void getAllPaginated_shouldReturnPageOfCategories_whenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDomain> page = new PageImpl<>(Arrays.asList(categoryDomain));
        when(repository.getAllPaginated(0, 10)).thenReturn(page);

        Page<CategoryDomain> result = useCase.getAllPaginated(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryDomain.getId(), result.getContent().get(0).getId());
        verify(repository, times(1)).getAllPaginated(0, 10);
    }

    @Test
    void getAllPaginated_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(repository.getAllPaginated(0, 10)).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getAllPaginated(0, 10));

        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(repository, times(1)).getAllPaginated(0, 10);
    }
}
