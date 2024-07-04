package com.intcomex.intcomex_api.adapter.controller.models;

import com.intcomex.intcomex_api.domain.model.ProductDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id;

    @NonNull
    @NotBlank(message = "Name is mandatory")
    @Schema(description = "The name of the product", example = "Laptop", required = true)
    private String name;

    @NonNull
    @NotNull(message = "Price is mandatory")
    @Schema(description = "The price of the product", example = "999.99", required = true)
    private BigDecimal price;

    @NonNull
    @NotNull(message = "Category ID is mandatory")
    @Schema(description = "The ID of the category the product belongs to", example = "1", required = true)
    private CategoryRequest category;

    public ProductDomain toDomain() {
        return ProductDomain.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .category(this.category.toDomain())
                .build();
    }
}
