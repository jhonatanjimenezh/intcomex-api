package com.intcomex.intcomex_api.application.port.out;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryStorageRepository {

    CategoryDomain save(CategoryDomain domain);
    List<CategoryDomain> getAll();
    CategoryDomain getById(Long id);
    Page<CategoryDomain> getAllPaginated(int page, int size);
    CategoryDomain update(CategoryDomain domain);
    void delete(Long id);
}
