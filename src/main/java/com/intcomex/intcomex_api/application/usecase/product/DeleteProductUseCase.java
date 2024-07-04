package com.intcomex.intcomex_api.application.usecase.product;

import com.intcomex.intcomex_api.application.port.in.product.DeleteProductPort;
import com.intcomex.intcomex_api.application.port.in.product.GetProductByIdPort;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteProductUseCase implements DeleteProductPort {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductUseCase.class);
    private final ProductStorageRepository repository;
    private final GetProductByIdPort getProductByIdPort;

    public DeleteProductUseCase(ProductStorageRepository repository, GetProductByIdPort getProductByIdPort) {
        this.repository = repository;
        this.getProductByIdPort = getProductByIdPort;
    }

    @Override
    public void delete(Long id) {
        try {
            logger.info("Trying to delete the product with ID: {}", id);

            ProductDomain existingProduct = getProductByIdPort.getById(id);
            if (existingProduct == null) {
                throw new CustomException(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), "Product not found");
            }

            repository.delete(id);
        } catch (DataBaseException d) {
            logger.error("Error deleting product: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), d);
        } catch (CustomException e) {
            logger.error("Error validating product: {}", e.getMessage());
            throw e;
        }
    }
}
