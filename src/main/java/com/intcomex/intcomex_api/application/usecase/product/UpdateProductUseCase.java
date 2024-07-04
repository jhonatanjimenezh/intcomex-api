package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.GetProductByIdPort;
import com.intcomex.intcomex_api.application.port.in.product.UpdateProductPort;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateProductUseCase implements UpdateProductPort {

    private static final Logger logger = LoggerFactory.getLogger(UpdateProductUseCase.class);
    private final ProductStorageRepository repository;
    private final GetCategoryByIdPort getCategoryByIdPort;
    private final GetProductByIdPort getProductByIdPort;

    public UpdateProductUseCase(ProductStorageRepository repository, GetCategoryByIdPort getCategoryByIdPort, GetProductByIdPort getProductByIdPort) {
        this.repository = repository;
        this.getCategoryByIdPort = getCategoryByIdPort;
        this.getProductByIdPort = getProductByIdPort;
    }

    @Override
    public ProductDomain update(ProductDomain domain) {
        try {
            logger.info("Trying to update the product: {}", domain);

            ProductDomain existingProduct = getProductByIdPort.getById(domain.getId());
            if (existingProduct == null) {
                throw new CustomException(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), "Product not found");
            }

            CategoryDomain category = getCategoryByIdPort.getById(domain.getCategory().getId());
            if (category == null) {
                throw new CustomException(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), "Category not found");
            }

            return repository.update(domain);
        } catch (DataBaseException d) {
            logger.error("Error updating product: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), d);
        } catch (CustomException e) {
            logger.error("Error validating product: {}", e.getMessage());
            throw e;
        }
    }
}
