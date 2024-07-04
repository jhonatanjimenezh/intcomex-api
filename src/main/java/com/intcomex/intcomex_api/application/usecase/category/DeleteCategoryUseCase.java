package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.in.category.DeleteCategoryPort;
import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DeleteCategoryUseCase implements DeleteCategoryPort {

    private static final Logger logger = LoggerFactory.getLogger(DeleteCategoryUseCase.class);
    private final CategoryStorageRepository repository;
    private final GetCategoryByIdPort getCategoryByIdPort;

    public DeleteCategoryUseCase(CategoryStorageRepository repository, GetCategoryByIdPort getCategoryByIdPort) {
        this.repository = repository;
        this.getCategoryByIdPort = getCategoryByIdPort;
    }

    @Override
    public void delete(Long id) {
        try {
            logger.info("Trying to delete the category with ID: {}", id);

            CategoryDomain existingCategory = getCategoryByIdPort.getById(id);
            if (existingCategory == null) {
                logger.error("Category not found with ID: {}", id);
                throw new CustomException(SPError.CONTROLLER_ERROR_DELETE.getErrorCode(), "Category not found");
            }

            repository.delete(id);
        } catch (DataBaseException d) {
            logger.error("Error deleting category: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), d);
        } catch (CustomException e) {
            logger.error("Error validating category: {}", e.getMessage());
            throw e;
        }
    }
}
