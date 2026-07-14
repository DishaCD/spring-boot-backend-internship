package com.example.employeesystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration for interactive API documentation.
 * UI available at {@code /swagger-ui/index.html}, docs at {@code /v3/api-docs}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System API")
                        .description(
                                "Production-style RESTful Employee Management System built with Spring Boot. "
                                        + "Provides complete CRUD operations for employee records with "
                                        + "DTO mapping, validation, and global exception handling.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Employee Management Team")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
