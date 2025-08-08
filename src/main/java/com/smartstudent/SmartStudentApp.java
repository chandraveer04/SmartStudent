/**
 * Smart Student Management System - Main Application Class
 * 
 * This is the entry point for the Smart Student Management System application.
 * It initializes the database connection, sets up the GUI look and feel,
 * and launches the login interface.
 * 
 * Features:
 * - Database connection testing
 * - System look and feel configuration
 * - Error handling for startup issues
 * - Login frame initialization
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent;

import com.smartstudent.gui.LoginFrame;
import com.smartstudent.util.DatabaseConnection;

import javax.swing.*;
// import java.sql.SQLException;

public class SmartStudentApp {
    
    /**
     * Main method - Entry point of the application
     * 
     * This method performs the following tasks:
     * 1. Sets the system look and feel for better UI appearance
     * 2. Tests the database connection
     * 3. Launches the login interface on the Event Dispatch Thread
     * 4. Handles any startup errors gracefully
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel for better UI appearance
        try {
            // Try to set the system look and feel
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Test database connection before launching the application
        System.out.println("Testing database connection...");
        DatabaseConnection.testConnection();
        
        // Launch application on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            } catch (Exception e) {
                // Handle any startup errors with user-friendly message
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
} 