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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllProductsUseCaseTest {

    @Mock
    private ProductStorageRepository repository;

    @InjectMocks
    private GetAllProductsUseCase useCase;

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
    void getAll_shouldReturnListOfProducts_whenSuccessful() {
        when(repository.getAll()).thenReturn(Arrays.asList(productDomain));

        List<ProductDomain> result = useCase.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDomain.getId(), result.get(0).getId());
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
    void getAllPaginated_shouldReturnPageOfProducts_whenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDomain> page = new PageImpl<>(Arrays.asList(productDomain));
        when(repository.getAllPaginated(0, 10)).thenReturn(page);

        Page<ProductDomain> result = useCase.getAllPaginated(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(productDomain.getId(), result.getContent().get(0).getId());
        verify(repository, times(1)).getAllPaginated(0, 10);
    }

    @Test
    void getAllPaginated_shouldThrowCustomException_whenDataBaseExceptionOccurs() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.getAllPaginated(0, 10)).thenThrow(new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> useCase.getAllPaginated(0, 10));

        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), exception.getErrorMessage());
        verify(repository, times(1)).getAllPaginated(0, 10);
    }
}
