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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCategoryUseCaseTest {

    @Mock
    private CategoryStorageRepository repository;

    @InjectMocks
    private CreateCategoryUseCase useCase;

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
    void create_shouldReturnSavedCategory_whenSuccessful() {
        when(repository.save(any(CategoryDomain.class))).thenReturn(categoryDomain);

        CategoryDomain result = useCase.create(categoryDomain);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(repository, times(1)).save(any(CategoryDomain.class));
    }

    @Test
    void create_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(repository.save(any(CategoryDomain.class))).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.create(categoryDomain));

        assertEquals(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(repository, times(1)).save(any(CategoryDomain.class));
    }
}
