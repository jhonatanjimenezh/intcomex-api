package com.intcomex.intcomex_api;

import com.intcomex.intcomex_api.config.exception.LoadAppException;
import com.intcomex.intcomex_api.config.exception.SPError;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IntcomexApiApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			throw new LoadAppException(SPError.APP_LOAD_ERROR.getErrorCode(), SPError.APP_LOAD_ERROR.getErrorMessage(), e);
		}
		SpringApplication.run(IntcomexApiApplication.class, args);
	}

}
