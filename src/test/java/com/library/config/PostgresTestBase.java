package com.library.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * Base class for integration tests. Starts a real PostgreSQL container and
 * connects the Spring context to it via Spring Boot's @ServiceConnection.
 *
 * The custom DataSourceConfig bean is @ConditionalOnProperty("DATABASE_URL"),
 * which is absent in the test environment, so Spring Boot's own auto-configuration
 * (fed by @ServiceConnection) provides the test datasource.
 */
@Testcontainers
@ImportAutoConfiguration(ServiceConnectionAutoConfiguration.class)
public abstract class PostgresTestBase {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("libms")
            .withUsername("libms_user")
            .withPassword("test");
}
