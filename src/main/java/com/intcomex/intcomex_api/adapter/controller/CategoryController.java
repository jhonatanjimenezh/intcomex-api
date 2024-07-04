package com.intcomex.intcomex_api.adapter.controller;

import com.intcomex.intcomex_api.adapter.controller.models.CategoryRequest;
import com.intcomex.intcomex_api.adapter.controller.models.CategoryResponse;
import com.intcomex.intcomex_api.application.port.in.category.*;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.ErrorResponse;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CreateCategoryPort createCategoryPort;
    private final DeleteCategoryPort deleteCategoryPort;
    private final GetAllCategoryPort getAllCategoryPort;
    private final GetCategoryByIdPort getCategoryByIdPort;
    private final UpdateCategoryPort updateCategoryPort;

    @Autowired
    public CategoryController(
            CreateCategoryPort createCategoryPort,
            DeleteCategoryPort deleteCategoryPort,
            GetAllCategoryPort getAllCategoryPort,
            GetCategoryByIdPort getCategoryByIdPort,
            UpdateCategoryPort updateCategoryPort) {
        this.createCategoryPort = createCategoryPort;
        this.deleteCategoryPort = deleteCategoryPort;
        this.getAllCategoryPort = getAllCategoryPort;
        this.getCategoryByIdPort = getCategoryByIdPort;
        this.updateCategoryPort = updateCategoryPort;
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryRequest request, BindingResult bindingResult) {
        try {
            logger.info("Received request to create category: {}", request);

            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getFieldErrors());
                return ResponseEntity.badRequest().body(CategoryResponse.badRequest(bindingResult));
            }

            CategoryDomain categoryDomain = request.toDomain();
            CategoryDomain createdCategory = createCategoryPort.create(categoryDomain);

            return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.of(createdCategory, HttpStatus.CREATED));
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_CREATED.getErrorCode(), SPError.CONTROLLER_ERROR_CREATED.getErrorMessage(), ex.getCause());
            logger.error("Post create category error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get all categories with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Object> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Received request to get all categories with pagination: page={}, size={}", page, size);
            Page<CategoryDomain> categories = getAllCategoryPort.getAllPaginated(page, size);
            return ResponseEntity.ok(CategoryResponse.of(categories.getContent(), HttpStatus.OK));
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_FIND_ALL.getErrorCode(), SPError.CONTROLLER_ERROR_FIND_ALL.getErrorMessage(), ex.getCause());
            logger.error("Get all categories error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id) {
        try {
            logger.info("Received request to get category by ID: {}", id);
            CategoryDomain category = getCategoryByIdPort.getById(id);
            return ResponseEntity.ok(CategoryResponse.of(category, HttpStatus.OK));
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Get category by ID error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorMessage(), ex.getCause());
            logger.error("Get category by ID error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Update category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request, BindingResult bindingResult) {
        try {
            logger.info("Received request to update category with ID: {}", id);

            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getFieldErrors());
                return ResponseEntity.badRequest().body(CategoryResponse.badRequest(bindingResult));
            }

            CategoryDomain categoryDomain = request.toDomain();
            categoryDomain.setId(id);
            CategoryDomain updatedCategory = updateCategoryPort.update(categoryDomain);
            return ResponseEntity.ok(CategoryResponse.of(updatedCategory, HttpStatus.OK));
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Update category error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), SPError.CONTROLLER_ERROR_UPDATE.getErrorMessage(), ex.getCause());
            logger.error("Update category error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Delete category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        try {
            logger.info("Received request to delete category with ID: {}", id);

            deleteCategoryPort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Delete category error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), SPError.CONTROLLER_ERROR_DELETE.getErrorMessage(), ex.getCause());
            logger.error("Delete category error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
