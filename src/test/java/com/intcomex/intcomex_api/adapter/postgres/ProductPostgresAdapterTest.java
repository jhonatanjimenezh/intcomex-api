package com.intcomex.intcomex_api.adapter.postgres;

import com.intcomex.intcomex_api.adapter.postgres.models.ProductEntity;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductPostgresAdapterTest {

    @Mock
    private ProductPostgresRepository repository;

    @InjectMocks
    private ProductPostgresAdapter adapter;

    private ProductDomain productDomain;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CategoryDomain category = new CategoryDomain();
        category.setId(1L);

        productDomain = new ProductDomain(1L, "Laptop", new BigDecimal("1500.00"), category);
        productEntity = ProductEntity.fromDomain(productDomain);
    }

    @Test
    void save_shouldReturnSavedProduct_whenSuccessful() {
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDomain result = adapter.save(productDomain);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(repository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void save_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        when(repository.save(any(ProductEntity.class))).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> adapter.save(productDomain));
        verify(repository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void getAll_shouldReturnListOfProducts_whenSuccessful() {
        when(repository.findAll()).thenReturn(Arrays.asList(productEntity));

        List<ProductDomain> result = adapter.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDomain.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        when(repository.findAll()).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> adapter.getAll());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnProduct_whenFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(productEntity));

        ProductDomain result = adapter.getById(1L);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldThrowDataBaseException_whenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> adapter.getById(1L));
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllPaginated_shouldReturnPageOfProducts_whenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> page = new PageImpl<>(Arrays.asList(productEntity));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<ProductDomain> result = adapter.getAllPaginated(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(productDomain.getId(), result.getContent().get(0).getId());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void getAllPaginated_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> adapter.getAllPaginated(0, 10));
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void update_shouldReturnUpdatedProduct_whenSuccessful() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDomain result = adapter.update(productDomain);

        assertNotNull(result);
        assertEquals(productDomain.getId(), result.getId());
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void update_shouldThrowDataBaseException_whenNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThrows(DataBaseException.class, () -> adapter.update(productDomain));
        verify(repository, times(1)).existsById(anyLong());
    }

    @Test
    void delete_shouldPerformDelete_whenSuccessful() {
        doNothing().when(repository).deleteById(anyLong());

        assertDoesNotThrow(() -> adapter.delete(1L));
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void delete_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        doThrow(RuntimeException.class).when(repository).deleteById(anyLong());

        assertThrows(DataBaseException.class, () -> adapter.delete(1L));
        verify(repository, times(1)).deleteById(anyLong());
    }
}
