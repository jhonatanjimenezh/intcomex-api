package com.intcomex.intcomex_api.adapter.postgres;

import com.intcomex.intcomex_api.adapter.postgres.models.ProductEntity;
import com.intcomex.intcomex_api.application.port.out.ProductStorageRepository;
import com.intcomex.intcomex_api.config.exception.DataBaseException;
import com.intcomex.intcomex_api.config.exception.SPError;
import com.intcomex.intcomex_api.domain.model.ProductDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductPostgresAdapter implements ProductStorageRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProductPostgresAdapter.class);
    private final ProductPostgresRepository repository;

    public ProductPostgresAdapter(ProductPostgresRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductDomain save(ProductDomain domain) {
        try {
            logger.info("Attempting to save product: {}", domain);
            ProductEntity entity = ProductEntity.fromDomain(domain);
            ProductEntity savedEntity = repository.save(entity);
            logger.info("Product saved successfully: {}", savedEntity.toDomain());
            return savedEntity.toDomain();
        } catch (Exception e) {
            logger.error("Error saving product: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_SAVE_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDomain> getAll() {
        try {
            logger.info("Retrieving all products from the database");
            return repository.findAll().stream()
                    .map(ProductEntity::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDomain getById(Long id) {
        try {
            logger.info("Retrieving product by ID: {}", id);
            return repository.findById(id)
                    .map(ProductEntity::toDomain)
                    .orElseThrow(() -> new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), "Product not found"));
        } catch (Exception e) {
            logger.error("Error retrieving product by ID: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDomain> getAllPaginated(int page, int size) {
        try {
            logger.info("Retrieving products paginated");
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable)
                    .map(ProductEntity::toDomain);
        } catch (Exception e) {
            logger.error("Error retrieving products paginated: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_FIND_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProductDomain update(ProductDomain domain) {
        try {
            logger.info("Attempting to update product: {}", domain);
            if (!repository.existsById(domain.getId())) {
                throw new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), "Product not found");
            }
            ProductEntity entity = ProductEntity.fromDomain(domain);
            ProductEntity updatedEntity = repository.save(entity);
            logger.info("Product updated successfully: {}", updatedEntity.toDomain());
            return updatedEntity.toDomain();
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_UPDATE_ERROR.getErrorMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            logger.info("Attempting to delete product by ID: {}", id);
            repository.deleteById(id);
            logger.info("Product deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            throw new DataBaseException(SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorCode(), SPError.DATABASE_ADAPTER_DELETE_ERROR.getErrorMessage(), e);
        }
    }
}
