package com.intcomex.intcomex_api.config.ddd;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.intcomex.intcomex_api.application.usecase",
        includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.intcomex\\.intcomex_api\\.application\\.usecase\\..*UseCase$")},
        useDefaultFilters = false
)
public class UsesCasesConfig {

}
