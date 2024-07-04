package com.intcomex.intcomex_api.application.port.in.category;

import com.intcomex.intcomex_api.domain.model.CategoryDomain;

public interface UpdateCategoryPort {
    CategoryDomain update(CategoryDomain domain);
}
