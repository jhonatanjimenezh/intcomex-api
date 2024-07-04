package com.intcomex.intcomex_api.application.usecase.category;


import com.intcomex.intcomex_api.application.port.in.category.CreateCategoryPort;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.CustomException;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateCategoryUseCase implements CreateCategoryPort {

    private static final Logger logger = LoggerFactory.getLogger(CreateCategoryUseCase.class);
    private final CategoryStorageRepository repository;

    public CreateCategoryUseCase(CategoryStorageRepository repository) {
        this.repository = repository;
    }

    public CategoryDomain create(CategoryDomain domain) {
        try {
            logger.info("Trying to save the category: {}", domain);
            return repository.save(domain);
        }catch (DataBaseException d){
            logger.error("Error saving category: {}", d.getMessage());
            throw new CustomException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), d);
        }
    }
}
