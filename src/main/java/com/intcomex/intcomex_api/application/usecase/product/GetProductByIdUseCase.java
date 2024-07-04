package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.GetProductByIdPort;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class GetProductByIdUseCase implements GetProductByIdPort {

    private static final Logger logger = LoggerFactory.getLogger(GetProductByIdUseCase.class);
    private final ProductStorageRepository repository;

    public GetProductByIdUseCase(ProductStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDomain getById(Long id) {
        try {
            logger.info("Trying to retrieve the product by ID: {}", id);

            ProductDomain existingProduct = repository.getById(id);
            if (existingProduct == null) {
                logger.error("Product not found with ID: {}", id);
                throw new CustomException(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), "Product not found");
            }

            return existingProduct;
        } catch (DataBaseException d) {
            logger.error("Error retrieving product by ID: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }
}
