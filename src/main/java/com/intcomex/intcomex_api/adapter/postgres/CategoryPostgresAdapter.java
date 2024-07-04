package com.intcomex.intcomex_api.adapter.postgres;

import com.intcomex.intcomex_api.adapter.postgres.models.CategoryEntity;
import com.intcomex.intcomex_api.application.port.out.CategoryStorageRepository;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.CategoryDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryPostgresAdapter implements CategoryStorageRepository {

    private static final Logger logger = LoggerFactory.getLogger(CategoryPostgresAdapter.class);
    private final CategoryPostgresRepository repository;

    public CategoryPostgresAdapter(CategoryPostgresRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CategoryDomain save(CategoryDomain domain) {
        try {
            logger.info("Attempting to save category: {}", domain);
            CategoryEntity entity = CategoryEntity.fromDomain(domain);
            CategoryEntity savedEntity = repository.save(entity);
            logger.info("Category saved successfully: {}", savedEntity.toDomain());
            return savedEntity.toDomain();
        } catch (Exception e) {
            logger.error("Error saving category : {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("categories")
    public List<CategoryDomain> getAll() {
        try {
            logger.info("Retrieving all transactions from the database");
            return repository.findAll().stream().map(CategoryEntity::toDomain).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving transactions: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDomain getById(Long id) {
        try {
            logger.info("Retrieving category by ID: {}", id);
            return repository.findById(id)
                    .map(CategoryEntity::toDomain)
                    .orElseThrow(() -> new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Category not found"));
        } catch (Exception e) {
            logger.error("Error retrieving category by ID: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDomain> getAllPaginated(int page, int size) {
        try {
            logger.info("Retrieving transactions paginated");
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable)
                    .map(CategoryEntity::toDomain);
        } catch (Exception e) {
            logger.error("Error retrieving transactions paginated: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional
    public CategoryDomain update(CategoryDomain domain) {
        try {
            logger.info("Attempting to update category : {}", domain);
            if (!repository.existsById(domain.getId())) {
                throw new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), "Category not found");
            }
            CategoryEntity entity = CategoryEntity.fromDomain(domain);
            CategoryEntity updatedEntity = repository.save(entity);
            logger.info("Category updated successfully: {}", updatedEntity.toDomain());
            return updatedEntity.toDomain();
        } catch (Exception e) {
            logger.error("Error updating category : {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            logger.info("Attempting to delete category by ID: {}", id);
            repository.deleteById(id);
            logger.info("Category deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting category : {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), e);
        }
    }
}
