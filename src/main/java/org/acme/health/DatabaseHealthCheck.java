package org.acme.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (Connection conn = dataSource.getConnection()) {
            return HealthCheckResponse.named("Database connection health check")
                    .up()
                    .withData("database", "H2")
                    .withData("status", "connected")
                    .build();
        } catch (SQLException e) {
            return HealthCheckResponse.named("Database connection health check")
                    .down()
                    .withData("error", e.getMessage())
                    .build();
        }
    }
} 