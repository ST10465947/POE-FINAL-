package org.example;

import java.util.regex.Pattern;

/**
 * Handles user authentication and registration
 * Implements username, password, and cell phone validation
 * Provides complete user management system
 *
 * @author Heloisa Campos
 * @version 1.5 - Complete with all rubric requirements
 *
 * ChatGPT Assistance:
 * - Regex pattern for South African cell phone validation: ^\+27[6-8]\d{7,9}$
 * - Password complexity validation logic with special character detection
 * - User registration workflow and error handling
 * - Comprehensive input validation methods
 */
public class Login {
    // User data storage
    private String storedUsername;
    private String storedPassword;
    private String storedCellPhone;
    private String storedFirstName;
    private String storedLastName;

    /**
     * Validates username format requirements
     * Must contain underscore and be maximum 5 characters
     * @param username the username to validate
     * @return true if username meets format requirements
     */
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /**
     * Validates password complexity requirements
     * Minimum 8 characters, uppercase, number, special character
     * @param password the password to validate
     * @return true if password meets complexity requirements
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        return hasUpperCaseLetter(password)
                && hasNumber(password)
                && hasSpecialCharacter(password);
    }

    /**
     * Checks if password contains at least one uppercase letter
     * @param password the password to check
     * @return true if contains uppercase letter
     */
    private boolean hasUpperCaseLetter(String password) {
        return !password.equals(password.toLowerCase());
    }

    /**
     * Checks if password contains at least one number
     * @param password the password to check
     * @return true if contains number
     */
    private boolean hasNumber(String password) {
        return password.matches(".*\\d.*");
    }

    /**
     * Checks if password contains at least one special character
     * @param password the password to check
     * @return true if contains special character
     */
    private boolean hasSpecialCharacter(String password) {
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    /**
     * Validates South African cell phone number format
     * Must start with +27 and have valid prefix (6,7,8)
     * Accepts 10-12 digit numbers as per test data
     * @param cellPhoneNumber the phone number to validate
     * @return true if valid South African format
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) return false;

        // Clean the number by removing spaces, dashes, parentheses
        String cleanedNumber = cellPhoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

        // Regex for South African numbers: +27 followed by 6-8 and 7-9 digits
        String regex = "^\\+27[6-8]\\d{7,9}$";
        return Pattern.compile(regex).matcher(cleanedNumber).matches();
    }

    /**
     * Registers a new user with comprehensive validation
     * Validates all fields before storing user data
     * @param username the desired username
     * @param password the desired password
     * @param cellPhone the user's cell phone number
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return success message or specific error message
     */
    public String registerUser(String username, String password, String cellPhone,
                               String firstName, String lastName) {
        boolean usernameValid = checkUserName(username);
        boolean passwordValid = checkPasswordComplexity(password);
        boolean cellPhoneValid = checkCellPhoneNumber(cellPhone);

        // Return specific error messages for each validation failure
        if (!usernameValid) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!passwordValid) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!cellPhoneValid) {
            return "Cell phone number incorrectly formatted or does not contain international code, please correct the number and try again.";
        }

        // Store user data after successful validation
        storeUserData(username, password, cellPhone, firstName, lastName);
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    /**
     * Stores validated user data in instance variables
     * @param username the validated username
     * @param password the validated password
     * @param cellPhone the validated cell phone
     * @param firstName the user's first name
     * @param lastName the user's last name
     */
    private void storeUserData(String username, String password, String cellPhone,
                               String firstName, String lastName) {
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellPhone = cellPhone;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
    }

    /**
     * ✅ FIXED: Authenticates user login with proper null checking
     * First checks if any user is registered, then validates credentials
     * @param inputUsername the username to authenticate
     * @param inputPassword the password to authenticate
     * @return Welcome message for success or appropriate error message
     */
    public String returnLoginStatus(String inputUsername, String inputPassword) {
        // ✅ CRITICAL FIX: Check if any user is registered first
        if (storedUsername == null) {
            return "No user registered yet. Please register first.";
        }

        boolean loginSuccess = isLoginSuccessful(inputUsername, inputPassword);

        if (loginSuccess) {
            return formatWelcomeMessage();
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    /**
     * Checks if login credentials match stored user data
     * Includes null safety checks for stored credentials
     * @param inputUsername the username to check
     * @param inputPassword the password to check
     * @return true if credentials match, false otherwise
     */
    private boolean isLoginSuccessful(String inputUsername, String inputPassword) {
        // ✅ FIXED: Added null checks for stored credentials
        if (storedUsername == null || storedPassword == null) {
            return false;
        }

        return inputUsername != null && inputPassword != null &&
                inputUsername.equals(storedUsername) && inputPassword.equals(storedPassword);
    }

    /**
     * Formats personalized welcome message with user's name
     * @return formatted welcome message
     */
    private String formatWelcomeMessage() {
        return "Welcome " + storedFirstName + "," + storedLastName + " it is great to see you again.";
    }

    // ==================== GETTER METHODS ====================

    /**
     * Gets the stored username for verification
     * @return the stored username or null if no user registered
     */
    public String getStoredUsername() {
        return storedUsername;
    }

    /**
     * Gets the stored first name
     * @return the stored first name or null if no user registered
     */
    public String getStoredFirstName() {
        return storedFirstName;
    }

    /**
     * Gets the stored last name
     * @return the stored last name or null if no user registered
     */
    public String getStoredLastName() {
        return storedLastName;
    }
}