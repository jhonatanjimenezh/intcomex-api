package com.intcomex.intcomex_api.adapter.postgres.models;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    public static CategoryEntity fromDomain(CategoryDomain domain){
        return CategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .imageUrl(domain.getImageUrl())
                .build();
    }

    public CategoryDomain toDomain(){
        return CategoryDomain.builder()
                .id(this.id)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
