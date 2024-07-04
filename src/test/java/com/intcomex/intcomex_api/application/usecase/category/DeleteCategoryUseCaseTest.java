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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DeleteCategoryUseCaseTest {

    @Mock
    private CategoryStorageRepository repository;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @InjectMocks
    private DeleteCategoryUseCase useCase;

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
    void delete_shouldPerformDelete_whenCategoryExists() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        doNothing().when(repository).delete(anyLong());

        assertDoesNotThrow(() -> useCase.delete(1L));
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).delete(anyLong());
    }

    @Test
    void delete_shouldThrowCustomException_whenCategoryNotFound() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.delete(1L));

        assertEquals(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), exception.getErrorCode());
        assertEquals("Category not found", exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, never()).delete(anyLong());
    }

    @Test
    void delete_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        doThrow(new DataBaseException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), "Database error")).when(repository).delete(anyLong());

        CustomException exception = assertThrows(CustomException.class, () -> useCase.delete(1L));

        assertEquals(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).delete(anyLong());
    }
}
