// src/test/java/com/intcomex/intcomex_api/integration/CategoryControllerIntegrationTest.java
package com.intcomex.intcomex_api.integration;

import com.intcomex.intcomex_api.adapter.controller.models.CategoryRequest;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
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

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryStorageRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDomain categoryDomain;

    @BeforeEach
    void setUp() {
        categoryDomain = new CategoryDomain();
        categoryDomain.setName("Electronics");
        categoryDomain.setImageUrl("http://example.com/electronics.png");

        categoryDomain = repository.save(categoryDomain);
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");
        request.setImageUrl("http://example.com/books.png");

        ResultActions result = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(201))
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.data.name").value("Books"))
                .andExpect(jsonPath("$.data.imageUrl").value("http://example.com/books.png"));
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() throws Exception {
        ResultActions result = mockMvc.perform(get("/category")
                .param("page", "0")
                .param("size", "10"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data[0].name").value("Electronics"))
                .andExpect(jsonPath("$.data[0].imageUrl").value("http://example.com/electronics.png"));
    }

    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        Long id = categoryDomain.getId();

        ResultActions result = mockMvc.perform(get("/category/{id}", id));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.name").value("Electronics"))
                .andExpect(jsonPath("$.data.imageUrl").value("http://example.com/electronics.png"));
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        Long id = categoryDomain.getId();

        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");
        request.setImageUrl("http://example.com/updated_electronics.png");

        ResultActions result = mockMvc.perform(put("/category/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code_status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.name").value("Updated Electronics"))
                .andExpect(jsonPath("$.data.imageUrl").value("http://example.com/updated_electronics.png"));
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        Long id = categoryDomain.getId();

        ResultActions result = mockMvc.perform(delete("/category/{id}", id));

        result.andExpect(status().isNoContent());
    }
}
