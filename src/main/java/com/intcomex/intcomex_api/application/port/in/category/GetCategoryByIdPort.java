package com.intcomex.intcomex_api.application.port.in.category;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;

public interface GetCategoryByIdPort {
    CategoryDomain getById(Long id);
}
