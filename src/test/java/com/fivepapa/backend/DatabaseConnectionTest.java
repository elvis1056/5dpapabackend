package com.fivepapa.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test PostgreSQL database connection
 *
 * Usage:
 * 1. Set environment variable DATABASE_URL to your Render PostgreSQL URL
 * 2. Run: SPRING_PROFILES_ACTIVE=prod ./mvnw test -Dtest=DatabaseConnectionTest
 */
@SpringBootTest
@ActiveProfiles("prod")
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws Exception {
        System.out.println("Testing database connection...");

        assertNotNull(dataSource, "DataSource should not be null");

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertTrue(connection.isValid(5), "Connection should be valid");

            System.out.println("✅ Database connection successful!");
            System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("URL: " + connection.getMetaData().getURL());
        }
    }
}
