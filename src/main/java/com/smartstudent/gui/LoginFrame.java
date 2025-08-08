/**
 * Login Frame - User Authentication Interface
 * 
 * Provides the login interface for the Smart Student Management System.
 * This class handles user authentication with a modern, gradient-based UI design.
 * 
 * Features:
 * - Secure user authentication
 * - Modern gradient background design
 * - Input validation and error handling
 * - Database connection testing
 * - Automatic transition to main application
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.gui;

import com.smartstudent.dao.UserDAO;
import com.smartstudent.model.User;
import com.smartstudent.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    
    // Data access object for authentication
    private UserDAO userDAO;

    /**
     * Constructor - Initializes the login frame
     * 
     * Sets up the login interface with all components, layout, and event handlers.
     * The frame is configured with a fixed size and centered on screen.
     */
    public LoginFrame() {
        userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Smart Student Management System - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Initializes all UI components
     * 
     * Creates and configures all input fields, buttons, and other UI elements
     * with appropriate styling and properties.
     */
    private void initializeComponents() {
        // Create input fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        // Create action buttons
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        
        // Apply styling to buttons
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
    }

    /**
     * Sets up the layout with gradient background
     * 
     * Creates a custom panel with gradient background and arranges all
     * components in a visually appealing layout using GridBagLayout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(135, 206, 250);
                Color color2 = new Color(70, 130, 180);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Smart Student Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Create login form panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);
        
        // Add password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        // Arrange components in main panel
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0; mainGbc.gridy = 0;
        mainGbc.insets = new Insets(20, 20, 10, 20);
        mainPanel.add(titlePanel, mainGbc);
        
        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(loginPanel, mainGbc);
        
        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(10, 20, 20, 20);
        mainPanel.add(buttonPanel, mainGbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up event handlers for all interactive components
     * 
     * Configures action listeners for buttons and sets the default button
     * for Enter key functionality.
     */
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Exit button action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Set default button for Enter key
        getRootPane().setDefaultButton(loginButton);
    }

    /**
     * Handles the login authentication process
     * 
     * Validates user input, tests database connection, authenticates user,
     * and transitions to the main application on successful login.
     * Provides appropriate error messages for various failure scenarios.
     */
    private void handleLogin() {
        // Get user input
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Test database connection first
            DatabaseConnection.testConnection();
            
            // Authenticate user
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                // Login successful
                JOptionPane.showMessageDialog(this, 
                    "Login successful! Welcome, " + user.getUsername(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Launch main application
                SwingUtilities.invokeLater(() -> {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                    dispose(); // Close login window
                });
            } else {
                // Login failed
                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password.", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (SQLException ex) {
            // Database connection error
            JOptionPane.showMessageDialog(this, 
                "Database connection error: " + ex.getMessage(), 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Main method for testing the login frame independently
     * 
     * This method can be used to test the login interface separately
     * from the main application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Try to set the system look and feel
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch login frame on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
} 