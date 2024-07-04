package com.intcomex.intcomex_api.adapter.controller;

import com.intcomex.intcomex_api.adapter.controller.models.ProductRequest;
import com.intcomex.intcomex_api.adapter.controller.models.ProductResponse;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.in.product.*;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.ErrorResponse;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final CreateProductPort createProductPort;
    private final DeleteProductPort deleteProductPort;
    private final GetAllProductsPort getAllProductsPort;
    private final GetProductByIdPort getProductByIdPort;
    private final UpdateProductPort updateProductPort;
    private final GetCategoryByIdPort getCategoryByIdPort;

    @Autowired
    public ProductController(
            CreateProductPort createProductPort,
            DeleteProductPort deleteProductPort,
            GetAllProductsPort getAllProductsPort,
            GetProductByIdPort getProductByIdPort,
            UpdateProductPort updateProductPort,
            GetCategoryByIdPort getCategoryByIdPort) {
        this.createProductPort = createProductPort;
        this.deleteProductPort = deleteProductPort;
        this.getAllProductsPort = getAllProductsPort;
        this.getProductByIdPort = getProductByIdPort;
        this.updateProductPort = updateProductPort;
        this.getCategoryByIdPort = getCategoryByIdPort;
    }

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductRequest request, BindingResult bindingResult) {
        try {
            logger.info("Received request to create product: {}", request);

            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getFieldErrors());
                return ResponseEntity.badRequest().body(ProductResponse.badRequest(bindingResult));
            }

            ProductDomain productDomain = request.toDomain();
            ProductDomain createdProduct = createProductPort.create(productDomain);

            return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.of(createdProduct, HttpStatus.CREATED));
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Post create product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_CREATED.getErrorCode(), SPError.CONTROLLER_ERROR_CREATED.getErrorMessage(), ex.getCause());
            logger.error("Post create product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get all products with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all products", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Received request to get all products with pagination: page={}, size={}", page, size);
            Page<ProductDomain> products = getAllProductsPort.getAllPaginated(page, size);
            return ResponseEntity.ok(ProductResponse.of(products.getContent(), HttpStatus.OK));
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_FIND_ALL.getErrorCode(), SPError.CONTROLLER_ERROR_FIND_ALL.getErrorMessage(), ex.getCause());
            logger.error("Get all products error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try {
            logger.info("Received request to get product by ID: {}", id);
            ProductDomain product = getProductByIdPort.getById(id);
            return ResponseEntity.ok(ProductResponse.of(product, HttpStatus.OK));
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Get product by ID error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorMessage(), ex.getCause());
            logger.error("Get product by ID error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request, BindingResult bindingResult) {
        try {
            logger.info("Received request to update product with ID: {}", id);

            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getFieldErrors());
                return ResponseEntity.badRequest().body(ProductResponse.badRequest(bindingResult));
            }

            ProductDomain productDomain = request.toDomain();
            productDomain.setId(id);
            ProductDomain updatedProduct = updateProductPort.update(productDomain);
            return ResponseEntity.ok(ProductResponse.of(updatedProduct, HttpStatus.OK));
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Update product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), SPError.CONTROLLER_ERROR_UPDATE.getErrorMessage(), ex.getCause());
            logger.error("Update product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        try {
            logger.info("Received request to delete product with ID: {}", id);

            deleteProductPort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, ex.getErrorCode(), ex.getErrorMessage(), null);
            logger.error("Delete product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(false, SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), SPError.CONTROLLER_ERROR_DELETE.getErrorMessage(), ex.getCause());
            logger.error("Delete product error: {}", errorResponse, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
