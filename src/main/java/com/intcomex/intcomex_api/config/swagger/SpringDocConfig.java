package com.intcomex.intcomex_api.config.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("intcomex-api")
                .packagesToScan("com.intcomex.intcomex_api")
                .addOpenApiCustomiser(openApi -> {
                    openApi.info(new Info()
                            .title("Intcomex API")
                            .version("v1")
                            .description("API for managing Intcomex"));
                })
                .build();
    }
}
