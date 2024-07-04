package com.intcomex.intcomex_api.application.usecase.category;

import com.intcomex.intcomex_api.application.port.in.category.GetAllCategoryPort;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.List;

public class GetAllCategoriesUseCase implements GetAllCategoryPort {

    private static final Logger logger = LoggerFactory.getLogger(GetAllCategoriesUseCase.class);
    private final CategoryStorageRepository repository;

    public GetAllCategoriesUseCase(CategoryStorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryDomain> getAll() {
        try {
            logger.info("Trying to retrieve all categories");
            return repository.getAll();
        } catch (DataBaseException d) {
            logger.error("Error retrieving categories: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }

    @Override
    public Page<CategoryDomain> getAllPaginated(int page, int size) {
        try {
            logger.info("Trying to retrieve all categories");
            return repository.getAllPaginated(page, size);
        } catch (DataBaseException d) {
            logger.error("Error retrieving categories: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), d);
        }
    }
}
