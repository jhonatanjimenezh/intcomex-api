package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.CreateProductPort;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class CreateProductUseCase implements CreateProductPort {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductUseCase.class);
    private final ProductStorageRepository repository;
    private final GetCategoryByIdPort getCategoryByIdPort;

    public CreateProductUseCase(ProductStorageRepository repository, GetCategoryByIdPort getCategoryByIdPort) {
        this.repository = repository;
        this.getCategoryByIdPort = getCategoryByIdPort;
    }

    @Override
    public ProductDomain create(ProductDomain domain) {
        try {
            logger.info("Trying to save the product: {}", domain);

            // Validar si la categoría existe
            CategoryDomain category = getCategoryByIdPort.getById(domain.getCategory().getId());
            if (category == null) {
                throw new CustomException(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), "Category not found");
            }

            return repository.save(domain);
        } catch (DataBaseException d) {
            logger.error("Error saving product: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), d);
        } catch (CustomException e) {
            logger.error("Error validating product: {}", e.getMessage());
            throw e;
        }
    }
}
