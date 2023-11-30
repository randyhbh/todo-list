package com.ss.challenge.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI(
            @Value("${spring.application.name}") String applicationName,
            @Value("${spring.application.version}") String applicationVersion,
            @Value("${spring.application.description}") String applicationDescription
    ) {
        return new OpenAPI()
                .info(new Info().title(applicationName)
                        .description(applicationDescription)
                        .version(applicationVersion)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
