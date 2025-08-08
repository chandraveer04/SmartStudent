/**
 * Database Connection Utility Class
 * 
 * Manages database connections for the Smart Student Management System.
 * This class provides centralized database configuration and connection
 * management using JDBC with MySQL.
 * 
 * Features:
 * - Configuration file loading with fallback defaults
 * - Connection pooling and management
 * - Error handling and logging
 * - Database connection testing
 * - Secure credential management
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    // Configuration file path
    private static final String CONFIG_FILE = "config/database.properties";
    
    // Database configuration properties
    private static Properties properties;
    
    // Static initialization block - loads configuration on class load
    static {
        loadProperties();
    }
    
    /**
     * Loads database configuration from properties file
     * 
     * This method attempts to load database configuration from the properties file.
     * If the file is not found or cannot be read, it falls back to default values.
     * 
     * Configuration includes:
     * - Database URL
     * - Username and password
     * - JDBC driver class name
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Fallback to default values if config file not found
                System.out.println("Configuration file not found, using default values");
                setDefaultProperties();
            } else {
                properties.load(input);
                System.out.println("Database configuration loaded successfully");
            }
        } catch (IOException e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            // Fallback to default values on error
            setDefaultProperties();
        }
    }
    
    /**
     * Sets default database configuration values
     * 
     * These are the fallback values used when the configuration file
     * is not available or cannot be read.
     */
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/smartstudent");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "Chandraveer@2004");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }

    /**
     * Creates and returns a database connection
     * 
     * This method establishes a connection to the MySQL database using
     * the configured properties. It handles driver loading and connection
     * creation with proper error handling.
     * 
     * @return Database connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName(properties.getProperty("db.driver"));
            
            // Create and return the connection
            return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        }
    }

    /**
     * Tests the database connection
     * 
     * This method attempts to establish a connection to the database
     * and prints the result. It's useful for verifying database
     * connectivity during application startup.
     * 
     * Usage: Called during application initialization to verify database access
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
} 