package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UpdateCategoryUseCaseTest {

    @Mock
    private CategoryStorageRepository repository;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @InjectMocks
    private UpdateCategoryUseCase useCase;

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
    void update_shouldReturnUpdatedCategory_whenSuccessful() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.update(any(CategoryDomain.class))).thenReturn(categoryDomain);

        CategoryDomain result = useCase.update(categoryDomain);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).update(any(CategoryDomain.class));
    }

    @Test
    void update_shouldThrowCustomException_whenCategoryNotFound() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.update(categoryDomain));

        assertEquals(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), exception.getErrorCode());
        assertEquals("Category not found", exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, never()).update(any(CategoryDomain.class));
    }

    @Test
    void update_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.update(any(CategoryDomain.class))).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.update(categoryDomain));

        assertEquals(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).update(any(CategoryDomain.class));
    }
}
