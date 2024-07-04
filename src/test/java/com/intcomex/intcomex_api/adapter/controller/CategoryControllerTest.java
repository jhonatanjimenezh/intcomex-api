package com.intcomex.intcomex_api.adapter.controller;

import com.intcomex.intcomex_api.adapter.controller.models.CategoryRequest;
import com.intcomex.intcomex_api.application.port.in.category.*;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class CategoryControllerTest {

    @Mock
    private CreateCategoryPort createCategoryPort;

    @Mock
    private DeleteCategoryPort deleteCategoryPort;

    @Mock
    private GetAllCategoryPort getAllCategoryPort;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @Mock
    private UpdateCategoryPort updateCategoryPort;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequest categoryRequest;
    private CategoryDomain categoryDomain;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
        categoryRequest.setImageUrl("http://example.com/electronics.png");

        categoryDomain = new CategoryDomain();
        categoryDomain.setId(1L);
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");

        bindingResult = mock(BindingResult.class);
    }

    @Test
    void createCategory_shouldReturnCreatedCategory_whenSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(createCategoryPort.create(any(CategoryDomain.class))).thenReturn(categoryDomain);

        ResponseEntity<Object> response = categoryController.createCategory(categoryRequest, bindingResult);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(createCategoryPort, times(1)).create(any(CategoryDomain.class));
    }

    @Test
    void createCategory_shouldReturnBadRequest_whenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = categoryController.createCategory(categoryRequest, bindingResult);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        verify(createCategoryPort, never()).create(any(CategoryDomain.class));
    }

    @Test
    void getAllCategories_shouldReturnCategories_whenSuccessful() {
        Page<CategoryDomain> page = new PageImpl<>(Arrays.asList(categoryDomain));
        when(getAllCategoryPort.getAllPaginated(anyInt(), anyInt())).thenReturn(page);

        ResponseEntity<Object> response = categoryController.getAllCategories(0, 10);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllCategoryPort, times(1)).getAllPaginated(anyInt(), anyInt());
    }

    @Test
    void getCategoryById_shouldReturnCategory_whenFound() {
        when(getCategoryByIdPort.getById(anyLong())).thenReturn(categoryDomain);

        ResponseEntity<Object> response = categoryController.getCategoryById(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
    }

    @Test
    void getCategoryById_shouldReturnNotFound_whenCategoryNotFound() {
        when(getCategoryByIdPort.getById(anyLong())).thenThrow(new CustomException(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), "Category not found"));

        ResponseEntity<Object> response = categoryController.getCategoryById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(getCategoryByIdPort, times(1)).getById(anyLong());
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory_whenSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(updateCategoryPort.update(any(CategoryDomain.class))).thenReturn(categoryDomain);

        ResponseEntity<Object> response = categoryController.updateCategory(1L, categoryRequest, bindingResult);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(updateCategoryPort, times(1)).update(any(CategoryDomain.class));
    }

    @Test
    void updateCategory_shouldReturnBadRequest_whenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = categoryController.updateCategory(1L, categoryRequest, bindingResult);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        verify(updateCategoryPort, never()).update(any(CategoryDomain.class));
    }

    @Test
    void deleteCategory_shouldReturnNoContent_whenSuccessful() {
        doNothing().when(deleteCategoryPort).delete(anyLong());

        ResponseEntity<Object> response = categoryController.deleteCategory(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(deleteCategoryPort, times(1)).delete(anyLong());
    }

    @Test
    void deleteCategory_shouldReturnNotFound_whenCategoryNotFound() {
        doThrow(new CustomException(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), "Category not found")).when(deleteCategoryPort).delete(anyLong());

        ResponseEntity<Object> response = categoryController.deleteCategory(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(deleteCategoryPort, times(1)).delete(anyLong());
    }
}
