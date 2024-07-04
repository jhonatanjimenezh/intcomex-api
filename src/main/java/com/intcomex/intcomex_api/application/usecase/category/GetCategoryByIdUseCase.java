package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.in.category.GetCategoryByIdPort;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCategoryByIdUseCase implements GetCategoryByIdPort {

    private static final Logger logger = LoggerFactory.getLogger(GetCategoryByIdUseCase.class);
    private final CategoryStorageRepository repository;
    public GetCategoryByIdUseCase(CategoryStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDomain getById(Long id) {
        try {
            logger.info("Trying to retrieve the category by ID: {}", id);

            CategoryDomain existingCategory = repository.getById(id);
            if (existingCategory == null) {
                logger.error("Category not found with ID: {}", id);
                throw new CustomException(SPError.CONTROLLER_ERROR_FIND_BY_ID.getErrorCode(), "Category not found");
            }

            return existingCategory;
        } catch (DataBaseException d) {
            logger.error("Error retrieving category by ID: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }
}
