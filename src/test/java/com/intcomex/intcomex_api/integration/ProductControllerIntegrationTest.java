package com.intcomex.intcomex_api.integration;

import com.intcomex.intcomex_api.adapter.controller.models.CategoryRequest;
import com.intcomex.intcomex_api.adapter.controller.models.ProductRequest;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryStorageRepository categoryRepository;

    @Autowired
    private ProductStorageRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDomain categoryDomain;
    private ProductDomain productDomain;

    @BeforeEach
    void setUp() {
        categoryDomain = new CategoryDomain();
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");

        categoryDomain = categoryRepository.save(categoryDomain);

        productDomain = new ProductDomain();
        productDomain.setName("Laptop");
        productDomain.setPrice(new BigDecimal("1500.00"));
        productDomain.setCategory(categoryDomain);

        productDomain = productRepository.save(productDomain);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(categoryDomain.getId());
        categoryRequest.setName(categoryDomain.getName());
        categoryRequest.setImageUrl(categoryDomain.getImageUrl());

        ProductRequest request = new ProductRequest();
        request.setName("Smartphone");
        request.setPrice(new BigDecimal("800.00"));
        request.setCategory(categoryRequest);

        ResultActions result = mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(201))
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.data.name").value("Smartphone"))
                .andExpect(jsonPath("$.data.price").value(800.00))
                .andExpect(jsonPath("$.data.category.id").value(categoryDomain.getId()));
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() throws Exception {
        ResultActions result = mockMvc.perform(get("/product")
                .param("page", "0")
                .param("size", "10"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].name").value("Laptop"))
                .andExpect(jsonPath("$.data[0].price").value(1500.00));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {
        Long id = productDomain.getId();

        ResultActions result = mockMvc.perform(get("/product/{id}", id));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.name").value("Laptop"))
                .andExpect(jsonPath("$.data.price").value(1500.00));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        Long id = productDomain.getId();

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(categoryDomain.getId());
        categoryRequest.setName(categoryDomain.getName());
        categoryRequest.setImageUrl(categoryDomain.getImageUrl());

        ProductRequest request = new ProductRequest();
        request.setName("Updated Laptop");
        request.setPrice(new BigDecimal("1600.00"));
        request.setCategory(categoryRequest);

        ResultActions result = mockMvc.perform(put("/product/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.data.price").value(1600.00));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        Long id = productDomain.getId();

        ResultActions result = mockMvc.perform(delete("/product/{id}", id));

        result.andExpect(status().isNoContent());
    }
}
