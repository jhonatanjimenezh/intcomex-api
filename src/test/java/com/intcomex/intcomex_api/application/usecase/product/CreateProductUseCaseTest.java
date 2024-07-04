package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
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

class CreateProductUseCaseTest {

    @Mock
    private ProductStorageRepository repository;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @InjectMocks
    private CreateProductUseCase useCase;

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
    void create_shouldReturnSavedProduct_whenSuccessful() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.save(any(ProductDomain.class))).thenReturn(productDomain);

        ProductDomain result = useCase.create(productDomain);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).save(any(ProductDomain.class));
    }

    @Test
    void create_shouldThrowCustomException_whenCategoryNotFound() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> useCase.create(productDomain));

        assertEquals(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), exception.getErrorCode());
        assertEquals("Category not found", exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, never()).save(any(ProductDomain.class));
    }

    @Test
    void create_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);
        when(repository.save(any(ProductDomain.class))).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.create(productDomain));

        assertEquals(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
        verify(repository, times(1)).save(any(ProductDomain.class));
    }
}
