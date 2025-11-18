package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Login class functionality
 * Tests user registration, authentication, and validation logic
 * Covers all edge cases and boundary conditions
 *
 * @author Heloisa Campos
 * @version 1.0 - Complete test coverage
 */
public class LoginTest {
    private Login login;

    /**
     * Sets up fresh Login instance before each test
     * Ensures test isolation and consistent starting state
     */
    @BeforeEach
    public void setUp() {
        login = new Login();
    }

    // ===== USERNAME VALIDATION TESTS =====

    /**
     * Tests valid username with underscore and 5 characters
     * Should pass validation according to specifications
     */
    @Test
    public void testValidUsername_WithUnderscoreAnd5Chars() {
        assertTrue(login.checkUserName("kyl_1"),
                "Username with underscore and 5 chars should be valid");
    }

    /**
     * Tests invalid username without underscore
     * Should fail validation due to missing underscore
     */
    @Test
    public void testInvalidUsername_NoUnderscore() {
        assertFalse(login.checkUserName("kyle1"),
                "Username without underscore should be invalid");
    }

    /**
     * Tests invalid username exceeding 5 character limit
     * Should fail validation due to length constraint
     */
    @Test
    public void testInvalidUsername_MoreThan5Chars() {
        assertFalse(login.checkUserName("very_long"),
                "Username with more than 5 chars should be invalid");
    }

    /**
     * Tests null username input
     * Should fail validation for null values
     */
    @Test
    public void testInvalidUsername_Null() {
        assertFalse(login.checkUserName(null),
                "Null username should be invalid");
    }

    // ===== PASSWORD COMPLEXITY TESTS =====

    /**
     * Tests valid password meeting all complexity requirements
     * 8+ chars, uppercase, number, special character
     */
    @Test
    public void testValidPassword_AllRequirements() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"),
                "Password with 8+ chars, uppercase, number, special char should be valid");
    }

    /**
     * Tests invalid password that is too short
     * Should fail minimum length requirement
     */
    @Test
    public void testInvalidPassword_TooShort() {
        assertFalse(login.checkPasswordComplexity("Short1!"),
                "Password shorter than 8 chars should be invalid");
    }

    /**
     * Tests invalid password without uppercase letters
     * Should fail uppercase requirement
     */
    @Test
    public void testInvalidPassword_NoUppercase() {
        assertFalse(login.checkPasswordComplexity("password1!"),
                "Password without uppercase should be invalid");
    }

    /**
     * Tests invalid password without numbers
     * Should fail numeric character requirement
     */
    @Test
    public void testInvalidPassword_NoNumber() {
        assertFalse(login.checkPasswordComplexity("Password!"),
                "Password without number should be invalid");
    }

    /**
     * Tests invalid password without special characters
     * Should fail special character requirement
     */
    @Test
    public void testInvalidPassword_NoSpecialChar() {
        assertFalse(login.checkPasswordComplexity("Password1"),
                "Password without special char should be invalid");
    }

    /**
     * Tests null password input
     * Should fail validation for null values
     */
    @Test
    public void testInvalidPassword_Null() {
        assertFalse(login.checkPasswordComplexity(null),
                "Null password should be invalid");
    }

    // ===== CELL PHONE VALIDATION TESTS =====

    /**
     * Tests valid South African cell phone with +27 prefix
     * Should pass validation for correct format
     */
    @Test
    public void testValidCellPhone_WithPlus27() {
        assertTrue(login.checkCellPhoneNumber("+27831234567"),
                "South African cell with +27 should be valid");
    }

    /**
     * Tests valid cell phones with different South African prefixes
     * Validates regex accepts all allowed prefixes (60, 71, 83)
     */
    @Test
    public void testValidCellPhone_WithValidPrefixes() {
        assertTrue(login.checkCellPhoneNumber("+27601234567"), "Prefix 60 should be valid");
        assertTrue(login.checkCellPhoneNumber("+27711234567"), "Prefix 71 should be valid");
        assertTrue(login.checkCellPhoneNumber("+27831234567"), "Prefix 83 should be valid");
    }

    /**
     * Tests invalid cell phone without international code
     * Should fail due to missing +27 prefix
     */
    @Test
    public void testInvalidCellPhone_NoInternationalCode() {
        assertFalse(login.checkCellPhoneNumber("0831234567"),
                "Cell without international code should be invalid");
    }

    /**
     * Tests invalid cell phone with wrong country code
     * Should fail for non-South African numbers
     */
    @Test
    public void testInvalidCellPhone_WrongCountryCode() {
        assertFalse(login.checkCellPhoneNumber("+44831234567"),
                "Cell with wrong country code should be invalid");
    }

    /**
     * Tests invalid cell phone missing plus sign
     * Should fail format validation
     */
    @Test
    public void testInvalidCellPhone_NoPlusSign() {
        assertFalse(login.checkCellPhoneNumber("27831234567"),
                "Cell without + sign should be invalid");
    }

    /**
     * Tests invalid cell phones with invalid prefixes
     * Should reject prefixes not in range 6-8
     */
    @Test
    public void testInvalidCellPhone_InvalidPrefix() {
        assertFalse(login.checkCellPhoneNumber("+27123456789"),
                "Cell with invalid prefix 12 should be invalid");
        assertFalse(login.checkCellPhoneNumber("+27992345678"),
                "Cell with invalid prefix 99 should be invalid");
    }

    /**
     * Tests null cell phone input
     * Should fail validation for null values
     */
    @Test
    public void testInvalidCellPhone_Null() {
        assertFalse(login.checkCellPhoneNumber(null),
                "Null cell phone should be invalid");
    }

    // ===== REGISTRATION FLOW TESTS =====

    /**
     * Tests successful user registration with all valid data
     * Validates complete registration workflow
     */
    @Test
    public void testSuccessfulRegistration_AllValidData() {
        String result = login.registerUser("te_st", "Password1!", "+27831234567", "John", "Doe");

        assertTrue(result.contains("successfully captured"),
                "Registration with valid data should succeed");
        assertEquals("te_st", login.getStoredUsername(),
                "Username should be stored after registration");
    }

    /**
     * Tests failed registration with invalid username
     * Should return appropriate error message
     */
    @Test
    public void testFailedRegistration_InvalidUsername() {
        String result = login.registerUser("invalid", "Password1!", "+27831234567", "John", "Doe");

        assertTrue(result.contains("Username is not correctly formatted"),
                "Registration with invalid username should fail");
    }

    /**
     * Tests failed registration with invalid password
     * Should return appropriate error message
     */
    @Test
    public void testFailedRegistration_InvalidPassword() {
        String result = login.registerUser("te_st", "weak", "+27831234567", "John", "Doe");

        assertTrue(result.contains("Password is not correctly formatted"),
                "Registration with invalid password should fail");
    }

    /**
     * Tests failed registration with invalid cell phone
     * Should return appropriate error message
     */
    @Test
    public void testFailedRegistration_InvalidCellPhone() {
        String result = login.registerUser("te_st", "Password1!", "0831234567", "John", "Doe");

        assertTrue(result.contains("Cell phone number incorrectly formatted"),
                "Registration with invalid cell phone should fail");
    }

    // ===== LOGIN AUTHENTICATION TESTS =====

    /**
     * Tests successful login with correct credentials
     * Validates authentication workflow and welcome message
     */
    @Test
    public void testSuccessfulLogin_CorrectCredentials() {
        // First register a user
        login.registerUser("us_er", "Password1!", "+27831234567", "Jane", "Smith");

        // Then test login
        String result = login.returnLoginStatus("us_er", "Password1!");

        assertTrue(result.startsWith("Welcome"),
                "Login with correct credentials should succeed");
        assertTrue(result.contains("Jane"), "Welcome message should contain first name");
        assertTrue(result.contains("Smith"), "Welcome message should contain last name");
    }

    /**
     * Tests failed login with incorrect username
     * Should return authentication error message
     */
    @Test
    public void testFailedLogin_IncorrectUsername() {
        login.registerUser("us_er", "Password1!", "+27831234567", "Jane", "Smith");

        String result = login.returnLoginStatus("wronguser", "Password1!");

        assertEquals("Username or password incorrect, please try again.", result,
                "Login with incorrect username should fail");
    }

    /**
     * Tests failed login with incorrect password
     * Should return authentication error message
     */
    @Test
    public void testFailedLogin_IncorrectPassword() {
        login.registerUser("us_er", "Password1!", "+27831234567", "Jane", "Smith");

        String result = login.returnLoginStatus("us_er", "WrongPassword!");

        assertEquals("Username or password incorrect, please try again.", result,
                "Login with incorrect password should fail");
    }

    /**
     * Tests failed login with null credentials
     * Should handle null values gracefully
     */
    @Test
    public void testFailedLogin_NullCredentials() {
        login.registerUser("us_er", "Password1!", "+27831234567", "Jane", "Smith");

        String result = login.returnLoginStatus(null, null);

        assertEquals("Username or password incorrect, please try again.", result,
                "Login with null credentials should fail");
    }

    /**
     * Tests login attempt when no user is registered
     * Should handle uninitialized state gracefully
     */
    @Test
    public void testLogin_NoUserRegistered() {
        Login freshLogin = new Login(); // Fresh instance with no registration

        String result = freshLogin.returnLoginStatus("anyuser", "anypassword");

        assertEquals("Username or password incorrect, please try again.", result,
                "Login when no user is registered should fail");
    }
}