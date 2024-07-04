package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseTest {

    @Mock
    private ProductStorageRepository repository;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @Mock
    private GetProductByIdPort getProductByIdPort;

    @InjectMocks
    private UpdateProductUseCase useCase;

    private ProductDomain productDomain;
    private CategoryDomain categoryDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryDomain = new CategoryDomain();
        categoryDomain.setId(1L);
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");

        productDomain = new ProductDomain();
        productDomain.setId(1L);
        productDomain.setName("Laptop");
        productDomain.setPrice(new BigDecimal("1500.00"));
        productDomain.setCategory(categoryDomain);
    }

    @Test
    void update_shouldReturnUpdatedProduct_whenSuccessful() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.update(any(ProductDomain.class))).thenReturn(productDomain);

        ProductDomain result = useCase.update(productDomain);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).update(any(ProductDomain.class));
    }

    @Test
    void update_shouldThrowCustomException_whenProductNotFound() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.update(productDomain));

        assertEquals(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), exception.getErrorCode());
        assertEquals("Product not found", exception.getErrorMessage());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(getCategoryByIdPort, never()).getById(anyLong());
        verify(repository, never()).update(any(ProductDomain.class));
    }

    @Test
    void update_shouldThrowCustomException_whenCategoryNotFound() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.update(productDomain));

        assertEquals(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), exception.getErrorCode());
        assertEquals("Category not found", exception.getErrorMessage());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, never()).update(any(ProductDomain.class));
    }

    @Test
    void update_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.update(any(ProductDomain.class))).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.update(productDomain));

        assertEquals(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(getProductByIdPort, times(1)).getById(anyLong());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).update(any(ProductDomain.class));
    }
}
