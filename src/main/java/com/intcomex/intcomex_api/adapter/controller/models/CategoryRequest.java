package com.intcomex.intcomex_api.adapter.controller.models;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @Schema(description = "The unique identifier of the category", example = "1")
    private Long id;

    @NonNull
    @NotBlank(message = "Name is mandatory")
    @Schema(description = "The name of the category", example = "SERVIDORES", required = true)
    private String name;

    @NonNull
    @NotBlank(message = "Image URL is mandatory")
    @Schema(description = "The image URL of the category", example = "http://intcomex.s3.aws.com/servidores.png", required = true)
    private String imageUrl;

    public CategoryDomain toDomain() {
        return CategoryDomain.builder()
                .id(this.id)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
