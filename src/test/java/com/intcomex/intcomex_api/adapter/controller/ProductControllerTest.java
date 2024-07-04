package com.intcomex.intcomex_api.adapter.controller;

import com.intcomex.intcomex_api.adapter.controller.models.CategoryRequest;
import com.intcomex.intcomex_api.adapter.controller.models.ProductRequest;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.in.product.*;
import com.intcomex.intcomex_api.config.exception.CustomException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class ProductControllerTest {

    @Mock
    private CreateProductPort createProductPort;

    @Mock
    private DeleteProductPort deleteProductPort;

    @Mock
    private GetAllProductsPort getAllProductsPort;

    @Mock
    private GetProductByIdPort getProductByIdPort;

    @Mock
    private UpdateProductPort updateProductPort;

    @Mock
    private GetCategoryByIdPort getCategoryByIdPort;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;
    private ProductDomain productDomain;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CategoryDomain categoryDomain = new CategoryDomain();
        categoryDomain.setId(1L);
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(1L);
        productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setPrice(new BigDecimal("1500.00"));
        productRequest.setCategory(categoryRequest);

        productDomain = new ProductDomain();
        productDomain.setId(1L);
        productDomain.setName("Laptop");
        productDomain.setPrice(new BigDecimal("1500.00"));
        productDomain.setCategory(categoryDomain);

        bindingResult = mock(BindingResult.class);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct_whenSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(createProductPort.create(any(ProductDomain.class))).thenReturn(productDomain);

        ResponseEntity<Object> response = productController.createProduct(productRequest, bindingResult);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(createProductPort, times(1)).create(any(ProductDomain.class));
    }

    @Test
    void createProduct_shouldReturnBadRequest_whenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = productController.createProduct(productRequest, bindingResult);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        verify(createProductPort, never()).create(any(ProductDomain.class));
    }

    @Test
    void getAllProducts_shouldReturnProducts_whenSuccessful() {
        Page<ProductDomain> page = new PageImpl<>(Arrays.asList(productDomain));
        when(getAllProductsPort.getAllPaginated(anyInt(), anyInt())).thenReturn(page);

        ResponseEntity<Object> response = productController.getAllProducts(0, 10);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllProductsPort, times(1)).getAllPaginated(anyInt(), anyInt());
    }

    @Test
    void getProductById_shouldReturnProduct_whenFound() {
        when(getProductByIdPort.getById(anyLong())).thenReturn(productDomain);

        ResponseEntity<Object> response = productController.getProductById(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getProductByIdPort, times(1)).getById(anyLong());
    }

    @Test
    void getProductById_shouldReturnNotFound_whenProductNotFound() {
        when(getProductByIdPort.getById(anyLong())).thenThrow(new CustomException(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), "Product not found"));

        ResponseEntity<Object> response = productController.getProductById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(getProductByIdPort, times(1)).getById(anyLong());
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct_whenSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(updateProductPort.update(any(ProductDomain.class))).thenReturn(productDomain);

        ResponseEntity<Object> response = productController.updateProduct(1L, productRequest, bindingResult);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(updateProductPort, times(1)).update(any(ProductDomain.class));
    }

    @Test
    void updateProduct_shouldReturnBadRequest_whenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = productController.updateProduct(1L, productRequest, bindingResult);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        verify(updateProductPort, never()).update(any(ProductDomain.class));
    }

    @Test
    void deleteProduct_shouldReturnNoContent_whenSuccessful() {
        doNothing().when(deleteProductPort).delete(anyLong());

        ResponseEntity<Object> response = productController.deleteProduct(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(deleteProductPort, times(1)).delete(anyLong());
    }

    @Test
    void deleteProduct_shouldReturnNotFound_whenProductNotFound() {
        doThrow(new CustomException(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), "Product not found")).when(deleteProductPort).delete(anyLong());

        ResponseEntity<Object> response = productController.deleteProduct(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(deleteProductPort, times(1)).delete(anyLong());
    }
}
