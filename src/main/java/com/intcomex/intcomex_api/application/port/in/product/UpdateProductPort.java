package com.intcomex.intcomex_api.application.port.in.product;

import com.intcomex.intcomex_api.domain.model.ProductDomain;

public interface UpdateProductPort {
    ProductDomain update(ProductDomain domain);
}
