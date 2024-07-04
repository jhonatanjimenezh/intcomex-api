package com.intcomex.intcomex_api.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
public class RestConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow credentials to be included in the CORS requests.
        config.setAllowedOriginPatterns(Collections.singletonList("*")); // Allow all origins.
        config.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers.
        config.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods.
        source.registerCorsConfiguration("/**", config); // Apply these settings to all paths.
        return new CorsFilter(source);
    }
}
