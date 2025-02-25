package com.aplazo.bnpl.challenge.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration class for setting up Swagger/OpenAPI documentation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI specification for the application.
     *
     * @return an {@link OpenAPI} instance with the API's title, version, and description
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aplazo Challenge Backend API") // Title of the API
                        .version("1.0") // API version
                        .description("API for managing and retrieving information about BNPL challenge using REST."));
    }

}
