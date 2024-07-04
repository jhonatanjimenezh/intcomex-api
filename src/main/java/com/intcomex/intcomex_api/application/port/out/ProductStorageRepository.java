package com.intcomex.intcomex_api.application.port.out;

import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductStorageRepository {
    ProductDomain save(ProductDomain domain);
    List<ProductDomain> getAll();
    ProductDomain getById(Long id);
    Page<ProductDomain> getAllPaginated(int page, int size);
    ProductDomain update(ProductDomain domain);
    void delete(Long id);
}
