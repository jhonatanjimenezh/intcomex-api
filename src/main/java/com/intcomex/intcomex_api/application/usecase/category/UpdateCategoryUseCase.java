package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.in.category.UpdateCategoryPort;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCategoryUseCase implements UpdateCategoryPort {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCategoryUseCase.class);
    private final CategoryStorageRepository repository;
    private final GetCategoryByIdPort getCategoryByIdPort;

    public UpdateCategoryUseCase(CategoryStorageRepository repository, GetCategoryByIdPort getCategoryByIdPort) {
        this.repository = repository;
        this.getCategoryByIdPort = getCategoryByIdPort;
    }

    @Override
    public CategoryDomain update(CategoryDomain domain) {
        try {
            logger.info("Trying to update the category: {}", domain);

            CategoryDomain existingCategory = getCategoryByIdPort.getById(domain.getId());
            if (existingCategory == null) {
                logger.error("Category not found with ID: {}", domain.getId());
                throw new CustomException(SPError.CONTROLLER_ERROR_UPDATE.getErrorCode(), "Category not found");
            }

            return repository.update(domain);
        } catch (DataBaseException d) {
            logger.error("Error updating category: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), d);
        } catch (CustomException e) {
            logger.error("Error validating category: {}", e.getMessage());
            throw e;
        }
    }
}
