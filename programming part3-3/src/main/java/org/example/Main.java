package org.example;

import javax.swing.JOptionPane;
import java.util.Scanner;

/**
 * Main application class - Complete authentication and messaging system
 * Implements user registration, login, and QuickChat messaging features
 * Provides JOptionPane-based user interface for entire system
 *
 * @author Heloisa Campos
 * @version 2.1 - JOptionPane implementation for rubric compliance
 *
 * ChatGPT Assistance:
 * - JSON serialization configuration for message persistence
 * - Regex pattern for South African cell phone validation
 * - Unit test structure and edge case handling
 */
public class Main {
    // System components
    private static final Scanner scanner = new Scanner(System.in);
    private static final Login loginSystem = new Login();

    /**
     * Main entry point of the application
     * Runs the complete system with JOptionPane interface
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        displayWelcomeMessage();

        boolean exitProgram = false;
        while (!exitProgram) {
            int choice = displayJOptionPaneMenu();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    if (loginUser()) {
                        runMessageManager();
                    }
                    break;
                case 3:
                    runAllTests();
                    break;
                case 4:
                    exitProgram = confirmExit();
                    break;
                case -1: // User closed the dialog
                    exitProgram = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null,
                            "Invalid option! Please choose 1-4.",
                            "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!exitProgram) {
                JOptionPane.showMessageDialog(null,
                        "Press OK to continue...",
                        "Continue", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        displayGoodbyeMessage();
        scanner.close();
    }

    /**
     * Displays JOptionPane menu as required by rubric
     * @return user's menu choice (1-4) or -1 if canceled
     */
    private static int displayJOptionPaneMenu() {
        Object[] options = {"1 - Register", "2 - Login", "3 - Run Tests", "4 - Exit"};

        String choice = (String) JOptionPane.showInputDialog(
                null,
                "=== QUICKCHAT SYSTEM ===\n" +
                        "Please choose an option:\n\n" +
                        "1. Register new user\n" +
                        "2. Login and Access QuickChat\n" +
                        "3. Run validation tests\n" +
                        "4. Exit program",
                "Main Menu",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Convert choice to numeric value
        if (choice == null) return -1; // User canceled

        switch (choice) {
            case "1 - Register": return 1;
            case "2 - Login": return 2;
            case "3 - Run Tests": return 3;
            case "4 - Exit": return 4;
            default: return -1;
        }
    }

    /**
     * Displays welcome message using JOptionPane
     */
    private static void displayWelcomeMessage() {
        JOptionPane.showMessageDialog(null,
                "==========================================\n" +
                        "    WELCOME TO LOGIN & REGISTRATION SYSTEM\n" +
                        "==========================================",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays goodbye message when application exits
     */
    private static void displayGoodbyeMessage() {
        JOptionPane.showMessageDialog(null,
                "==========================================\n" +
                        "    Thank you for using our system!\n" +
                        "               Goodbye!\n" +
                        "==========================================",
                "Goodbye",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Confirms user wants to exit the application
     * @return true if user confirms exit, false otherwise
     */
    private static boolean confirmExit() {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Handles complete user registration workflow with JOptionPane
     */
    private static void registerUser() {
        JOptionPane.showMessageDialog(null,
                "=== USER REGISTRATION ===",
                "Registration",
                JOptionPane.INFORMATION_MESSAGE);

        String firstName = JOptionPane.showInputDialog("Enter first name:");
        if (firstName == null) return; // User canceled

        String lastName = JOptionPane.showInputDialog("Enter last name:");
        if (lastName == null) return;

        String username, password, cellPhone;
        String result;

        do {
            username = JOptionPane.showInputDialog(
                    "Enter username:\n(Must contain _ and be ≤5 characters)");
            if (username == null) return;

            password = JOptionPane.showInputDialog(
                    "Enter password:\n(Min 8 chars, capital, number, special char)");
            if (password == null) return;

            cellPhone = JOptionPane.showInputDialog(
                    "Enter cell phone number:\n(With international code, e.g., +27831234567)");
            if (cellPhone == null) return;

            result = loginSystem.registerUser(username, password, cellPhone, firstName, lastName);

            if (!result.equals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.")) {
                JOptionPane.showMessageDialog(null,
                        result + "\n\nPlease correct the errors and try again.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!result.equals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added."));

        JOptionPane.showMessageDialog(null,
                "Registration completed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles user login authentication with JOptionPane
     * @return true if login successful, false otherwise
     */
    private static boolean loginUser() {
        // Check if any user is registered
        if (loginSystem.getStoredUsername() == null) {
            JOptionPane.showMessageDialog(null,
                    "No user registered yet. Please register first.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String inputUsername = JOptionPane.showInputDialog("Enter username:");
        if (inputUsername == null) return false;

        String inputPassword = JOptionPane.showInputDialog("Enter password:");
        if (inputPassword == null) return false;

        String loginStatus = loginSystem.returnLoginStatus(inputUsername, inputPassword);

        if (loginStatus.startsWith("Welcome")) {
            JOptionPane.showMessageDialog(null, loginStatus, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, loginStatus, "Login Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Runs the Message Manager system after successful login
     */
    private static void runMessageManager() {
        JOptionPane.showMessageDialog(null,
                "Launching Message Management System...",
                "QuickChat Access",
                JOptionPane.INFORMATION_MESSAGE);

        // Run MessageManager main method
        MessageManager.main(new String[]{});
    }

    /**
     * Executes all validation tests and shows results
     */
    private static void runAllTests() {
        JOptionPane.showMessageDialog(null,
                "Running all validation tests...\nCheck console for detailed results.",
                "Test Runner",
                JOptionPane.INFORMATION_MESSAGE);

        TestRunner.runAllTests();

        JOptionPane.showMessageDialog(null,
                "All tests completed!\nCheck console for results.",
                "Tests Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
}