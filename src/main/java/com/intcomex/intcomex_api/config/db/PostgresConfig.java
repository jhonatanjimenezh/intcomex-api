package com.intcomex.intcomex_api.config.db;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
@EnableConfigurationProperties(DataSourcePostgresConfig.class)
public class PostgresConfig {

    private final DataSourcePostgresConfig dataSourcePostgresConfig;

    public PostgresConfig(DataSourcePostgresConfig dataSourcePostgresConfig) {
        this.dataSourcePostgresConfig = dataSourcePostgresConfig;
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourcePostgresConfig.getDriverClassName());
        dataSource.setUrl(dataSourcePostgresConfig.getUrl());
        dataSource.setUsername(dataSourcePostgresConfig.getUsername());
        dataSource.setPassword(dataSourcePostgresConfig.getPassword());
        return dataSource;
    }
}
