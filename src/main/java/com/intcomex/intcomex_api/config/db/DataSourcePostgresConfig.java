package com.intcomex.intcomex_api.config.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourcePostgresConfig {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
}
