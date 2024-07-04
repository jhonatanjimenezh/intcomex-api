package com.intcomex.intcomex_api.application.port.in.product;

import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GetAllProductsPort {
    List<ProductDomain> getAll();

    Page<ProductDomain> getAllPaginated(int page, int size);
}
