package com.intcomex.intcomex_api.adapter.postgres.models;

import com.intcomex.intcomex_api.domain.model.ProductDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public static ProductEntity fromDomain(ProductDomain domain) {
        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .price(domain.getPrice())
                .category(CategoryEntity.fromDomain(domain.getCategory()))
                .build();
    }

    public ProductDomain toDomain() {
        return ProductDomain.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .category(this.category.toDomain())
                .build();
    }
}
