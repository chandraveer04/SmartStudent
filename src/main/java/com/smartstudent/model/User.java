/**
 * User Model Class
 * 
 * Represents a user entity in the Smart Student Management System.
 * This class handles user authentication and authorization information,
 * including username, password, role, and audit timestamps.
 * 
 * Features:
 * - User authentication data storage
 * - Role-based access control support
 * - Audit trail with creation timestamps
 * - Secure password handling
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.model;

import java.time.LocalDateTime;

public class User {
    // Database primary key
    private int id;
    
    // Authentication credentials
    private String username;
    private String password;
    
    // Authorization information
    private String role;
    
    // Audit timestamp
    private LocalDateTime createdAt;

    /**
     * Default constructor for creating empty user objects
     */
    public User() {}

    /**
     * Constructor for creating new user records (without ID)
     * 
     * @param username User's login username
     * @param password User's login password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "admin"; // Default role
    }

    /**
     * Constructor for creating user records with database ID
     * 
     * @param id Database primary key
     * @param username User's login username
     * @param password User's login password
     * @param role User's role in the system
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ==================== GETTERS AND SETTERS ====================
    
    /**
     * Gets the database primary key
     * @return User ID
     */
    public int getId() { return id; }
    
    /**
     * Sets the database primary key
     * @param id User ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the user's login username
     * @return Username
     */
    public String getUsername() { return username; }
    
    /**
     * Sets the user's login username
     * @param username Username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets the user's login password
     * @return Password (should be hashed in production)
     */
    public String getPassword() { return password; }
    
    /**
     * Sets the user's login password
     * @param password Password (should be hashed in production)
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Gets the user's role in the system
     * @return Role (e.g., "admin", "user")
     */
    public String getRole() { return role; }
    
    /**
     * Sets the user's role in the system
     * @param role Role (e.g., "admin", "user")
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Gets the record creation timestamp
     * @return Creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Sets the record creation timestamp
     * @param createdAt Creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ==================== OVERRIDE METHODS ====================
    
    /**
     * Returns a string representation of the user
     * 
     * Note: Password is not included for security reasons
     * 
     * @return Formatted string with user details (excluding password)
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', role='%s'}", id, username, role);
    }
} 