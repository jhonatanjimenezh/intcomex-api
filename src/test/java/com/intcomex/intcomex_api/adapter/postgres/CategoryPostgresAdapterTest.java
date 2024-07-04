package com.intcomex.intcomex_api.adapter.postgres;

import com.intcomex.intcomex_api.adapter.postgres.models.CategoryEntity;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryPostgresAdapterTest {

    @Mock
    private CategoryPostgresRepository repository;

    @InjectMocks
    private CategoryPostgresAdapter adapter;

    private CategoryDomain categoryDomain;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryDomain = new CategoryDomain(1L, "Electronics", "http://example.com/electronics.png");
        categoryEntity = CategoryEntity.fromDomain(categoryDomain);
    }

    @Test
    void save_shouldReturnSavedCategory_whenSuccessful() {
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        CategoryDomain result = adapter.save(categoryDomain);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(repository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void save_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        when(repository.save(any(CategoryEntity.class))).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> adapter.save(categoryDomain));
        verify(repository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void getAll_shouldReturnListOfCategories_whenSuccessful() {
        when(repository.findAll()).thenReturn(Arrays.asList(categoryEntity));

        List<CategoryDomain> result = adapter.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDomain.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_shouldThrowDataBaseException_whenRepositoryThrowsException() {
        when(repository.findAll()).thenThrow(RuntimeException.class);

        assertThrows(DataBaseException.class, () -> adapter.getAll());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnCategory_whenFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));

        CategoryDomain result = adapter.getById(1L);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldThrowDataBaseException_whenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> adapter.getById(1L));
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllPaginated_shouldReturnPageOfCategories_whenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryEntity> page = new PageImpl<>(Arrays.asList(categoryEntity));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<CategoryDomain> result = adapter.getAllPaginated(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryDomain.getId(), result.getContent().get(0).getId());
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
    void update_shouldReturnUpdatedCategory_whenSuccessful() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        CategoryDomain result = adapter.update(categoryDomain);

        assertNotNull(result);
        assertEquals(categoryDomain.getId(), result.getId());
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void update_shouldThrowDataBaseException_whenNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThrows(DataBaseException.class, () -> adapter.update(categoryDomain));
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
