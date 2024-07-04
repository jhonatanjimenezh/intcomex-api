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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class GetCategoryByIdUseCaseTest {

    @Mock
    private CategoryStorageRepository repository;

    @InjectMocks
    private GetCategoryByIdUseCase useCase;

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
    void getById_shouldReturnCategory_whenFound() {
        when(repository.getById(anyLong())).thenReturn(categoryDomain);

        CategoryDomain result = useCase.getById(1L);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(repository, times(1)).getById(anyLong());
    }

    @Test
    void getById_shouldThrowCustomException_whenCategoryNotFound() {
        when(repository.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getById(1L));

        assertEquals(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), exception.getErrorCode());
        assertEquals("Category not found", exception.getErrorMessage());
        verify(repository, times(1)).getById(anyLong());
    }

    @Test
    void getById_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(repository.getById(anyLong())).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getById(1L));

        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(repository, times(1)).getById(anyLong());
    }
}
