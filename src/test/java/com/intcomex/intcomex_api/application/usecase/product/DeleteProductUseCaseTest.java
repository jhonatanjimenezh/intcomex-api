package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.GetProductByIdPort;
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

class DeleteProductUseCaseTest {

    @Mock
    private ProductStorageRepository repository;

    @Mock
    private GetProductByIdPort getProductByIdPort;

    @InjectMocks
    private DeleteProductUseCase useCase;

    private ProductDomain productDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productDomain = new ProductDomain();
        productDomain.setId(1L);
        productDomain.setName("Laptop");
        productDomain.setPrice(new BigDecimal("1500.00"));
        CategoryDomain category = new CategoryDomain();
        category.setId(1L);
        productDomain.setCategory(category);
    }

    @Test
    void delete_shouldPerformDelete_whenProductExists() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);
        doNothing().when(repository).delete(anyLong());

        assertDoesNotThrow(() -> useCase.delete(1L));
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).delete(anyLong());
    }

    @Test
    void delete_shouldThrowCustomException_whenProductNotFound() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.delete(1L));

        assertEquals(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), exception.getErrorCode());
        assertEquals("Product not found", exception.getErrorMessage());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(repository, never()).delete(anyLong());
    }

    @Test
    void delete_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);
        doThrow(new DataBaseException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), "Database error")).when(repository).delete(anyLong());

        CustomException exception = assertThrows(CustomException.class, () -> useCase.delete(1L));

        assertEquals(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).delete(anyLong());
    }
}
