package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class GetProductByIdUseCaseTest {

    @Mock
    private ProductStorageRepository repository;

    @InjectMocks
    private GetProductByIdUseCase useCase;

    private ProductDomain productDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CategoryDomain categoryDomain = new CategoryDomain();
        categoryDomain.setId(1L);

        productDomain = new ProductDomain();
        productDomain.setId(1L);
        productDomain.setName("Laptop");
        productDomain.setPrice(new BigDecimal("1500.00"));
        productDomain.setCategory(categoryDomain);
    }

    @Test
    void getById_shouldReturnProduct_whenFound() {
        when(repository.getById(anyLong())).thenReturn(productDomain);

        ProductDomain result = useCase.getById(1L);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(repository, times(1)).getById(anyLong());
    }

    @Test
    void getById_shouldThrowCustomException_whenProductNotFound() {
        when(repository.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getById(1L));

        assertEquals(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), exception.getErrorCode());
        assertEquals("Product not found", exception.getErrorMessage());
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
