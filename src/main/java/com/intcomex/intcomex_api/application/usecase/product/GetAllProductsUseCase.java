package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.GetAllProductsPort;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.List;


public class GetAllProductsUseCase implements GetAllProductsPort {

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsUseCase.class);
    private final ProductStorageRepository repository;

    public GetAllProductsUseCase(ProductStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductDomain> getAll() {
        try {
            logger.info("Trying to retrieve all products");
            return repository.getAll();
        } catch (DataBaseException d) {
            logger.error("Error retrieving products: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }

    @Override
    public Page<ProductDomain> getAllPaginated(int page, int size) {
        try {
            logger.info("Trying to retrieve all products to paginate");
            return repository.getAllPaginated(page, size);
        } catch (DataBaseException d) {
            logger.error("Error retrieving products to paginate: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }
}
