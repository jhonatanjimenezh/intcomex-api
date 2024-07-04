package com.intcomex.intcomex_api.adapter.postgres;

import com.intcomex.intcomex_api.adapter.postgres.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPostgresRepository extends JpaRepository<ProductEntity, Long> {

}
