package com.intcomex.intcomex_api.application.port.in.category;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GetAllCategoryPort {
    List<CategoryDomain> getAll();

    Page<CategoryDomain> getAllPaginated(int page, int size);
}
