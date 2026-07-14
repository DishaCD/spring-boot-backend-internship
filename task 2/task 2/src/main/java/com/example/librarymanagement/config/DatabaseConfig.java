package com.example.librarymanagement.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.librarymanagement.repository")
@EntityScan(basePackages = "com.example.librarymanagement.entity")
public class DatabaseConfig {
    // Custom database configurations and transaction management best practices
}
