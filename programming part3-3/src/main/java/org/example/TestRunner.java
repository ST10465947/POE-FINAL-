package org.example;

/**
 * Comprehensive test runner for all system components
 * Executes unit tests for login, messaging, and validation
 * Provides detailed test results and coverage reporting
 *
 * @author Heloisa Campos
 * @version 1.0 - Complete test suite
 */
public class TestRunner {

    /**
     * Executes all test suites in the system
     * Runs comprehensive validation of all functionality
     */
    public static void runAllTests() {
        runLoginTests();
        runMessageTests();
        runRequiredTestCases();
        runChatGPTSpecificTests();
        runBoundaryConditionTests();
        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }

    /**
     * Tests login functionality including validation
     * Covers username, password, and cell phone validation
     */
    private static void runLoginTests() {
        System.out.println("\n--- LOGIN TESTS ---");
        Login login = new Login();
        int passed = 0;
        int total = 0;

        // Test valid username format
        total++;
        boolean test1 = login.checkUserName("kyl_1");
        System.out.println("Test 1 - Valid username: " + (test1 ? "✓ PASS" : "✗ FAIL"));
        if (test1) passed++;

        // Test invalid username format
        total++;
        boolean test2 = !login.checkUserName("kyle!!!!!!!");
        System.out.println("Test 2 - Invalid username: " + (test2 ? "✓ PASS" : "✗ FAIL"));
        if (test2) passed++;

        // Test valid password complexity
        total++;
        boolean test3 = login.checkPasswordComplexity("Ch&&sec@ke99!");
        System.out.println("Test 3 - Valid password: " + (test3 ? "✓ PASS" : "✗ FAIL"));
        if (test3) passed++;

        // Test invalid password complexity
        total++;
        boolean test4 = !login.checkPasswordComplexity("password");
        System.out.println("Test 4 - Invalid password: " + (test4 ? "✓ PASS" : "✗ FAIL"));
        if (test4) passed++;

        // Test valid cell phone format
        total++;
        boolean test5 = login.checkCellPhoneNumber("+2783123456");
        System.out.println("Test 5 - Valid cell phone: " + (test5 ? "✓ PASS" : "✗ FAIL"));
        if (test5) passed++;

        // Test invalid cell phone format
        total++;
        boolean test6 = !login.checkCellPhoneNumber("08966553");
        System.out.println("Test 6 - Invalid cell phone: " + (test6 ? "✓ PASS" : "✗ FAIL"));
        if (test6) passed++;

        System.out.println("Login Tests: " + passed + "/" + total + " passed");
    }

    /**
     * Tests message functionality and validation
     * Covers message creation, validation, and processing
     */
    private static void runMessageTests() {
        System.out.println("\n--- MESSAGE TESTS ---");
        Message.resetMessageCount();
        int passed = 0;
        int total = 0;

        // Test message ID validation
        total++;
        Message message1 = new Message("+271234567", "Hello World", 1);
        boolean test1 = message1.checkMessageID();
        System.out.println("Test 1 - Valid message ID: " + (test1 ? "✓ PASS" : "✗ FAIL"));
        if (test1) passed++;

        // Test recipient validation
        total++;
        boolean test2 = message1.checkRecipientCell() == 1;
        System.out.println("Test 2 - Valid recipient: " + (test2 ? "✓ PASS" : "✗ FAIL"));
        if (test2) passed++;

        // Test recipient validation message
        total++;
        String recipientMsg = message1.validateRecipient();
        boolean test3 = recipientMsg.equals("Cell phone number successfully captured.");
        System.out.println("Test 3 - Valid recipient message: " + (test3 ? "✓ PASS" : "✗ FAIL"));
        if (test3) passed++;

        // Test invalid recipient
        total++;
        Message message2 = new Message("0831234567", "Test", 2);
        boolean test4 = message2.checkRecipientCell() == 0;
        System.out.println("Test 4 - Invalid recipient: " + (test4 ? "✓ PASS" : "✗ FAIL"));
        if (test4) passed++;

        // Test message hash generation
        total++;
        Message message3 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);
        String hash = message3.createMessageHash();
        boolean test5 = hash.matches("\\d{2}:0:HITONIGHT");
        System.out.println("Test 5 - Message hash exact format: " + (test5 ? "✓ PASS" : "✗ FAIL"));
        if (test5) passed++;

        // Test message length validation (over limit)
        total++;
        Message longMessage = new Message("+271234567", "A".repeat(251), 1);
        String lengthValidation = longMessage.validateMessageLength();
        boolean test6 = lengthValidation.equals("Message exceeds 250 characters by 1, please reduce size.");
        System.out.println("Test 6 - Message length validation (251 chars): " + (test6 ? "✓ PASS" : "✗ FAIL"));
        if (test6) passed++;

        // Test message length validation (exact limit)
        total++;
        Message exactMessage = new Message("+271234567", "A".repeat(250), 2);
        String exactValidation = exactMessage.validateMessageLength();
        boolean test7 = exactValidation.equals("Message ready to send.");
        System.out.println("Test 7 - Exact 250 character message: " + (test7 ? "✓ PASS" : "✗ FAIL"));
        if (test7) passed++;

        // Test message length validation (under limit)
        total++;
        Message shortMessage = new Message("+271234567", "Short", 3);
        String shortValidation = shortMessage.validateMessageLength();
        boolean test8 = shortValidation.equals("Message ready to send.");
        System.out.println("Test 8 - Short message validation: " + (test8 ? "✓ PASS" : "✗ FAIL"));
        if (test8) passed++;

        System.out.println("Message Tests: " + passed + "/" + total + " passed");
    }

    /**
     * Tests exact test cases from specification document
     * Validates system against provided test data
     */
    private static void runRequiredTestCases() {
        System.out.println("\n--- REQUIRED TEST CASES FROM DOCUMENT ---");
        Message.resetMessageCount();
        int passed = 0;
        int total = 0;

        // Test Case 1: Valid message with exact specification data
        total++;
        Message message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);
        String lengthValidation = message1.validateMessageLength();
        int recipientValidation = message1.checkRecipientCell();
        String recipientMessage = message1.validateRecipient();
        boolean case1 = lengthValidation.equals("Message ready to send.") &&
                recipientValidation == 1 &&
                recipientMessage.equals("Cell phone number successfully captured.");
        System.out.println("Required Case 1 - Message 1 validation: " + (case1 ? "✓ PASS" : "✗ FAIL"));
        if (case1) passed++;

        // Test Case 2: Invalid recipient number
        total++;
        Message message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?", 2);
        int recipientValidation2 = message2.checkRecipientCell();
        String recipientMessage2 = message2.validateRecipient();
        boolean case2 = recipientValidation2 == 0 &&
                recipientMessage2.equals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
        System.out.println("Required Case 2 - Message 2 validation: " + (case2 ? "✓ PASS" : "✗ FAIL"));
        if (case2) passed++;

        // Test Case 3: Hash format validation
        total++;
        String hash = message1.createMessageHash();
        boolean case3 = hash.matches("\\d{2}:0:HITONIGHT");
        System.out.println("Required Case 3 - Hash format HITONIGHT: " + (case3 ? "✓ PASS" : "✗ FAIL"));
        if (case3) passed++;

        // Test Case 4: Message length boundary test
        total++;
        Message longMessage = new Message("+271234567", "A".repeat(251), 3);
        String lengthCheck = longMessage.validateMessageLength();
        boolean case4 = lengthCheck.equals("Message exceeds 250 characters by 1, please reduce size.");
        System.out.println("Required Case 4 - Message length validation (251 chars): " + (case4 ? "✓ PASS" : "✗ FAIL"));
        if (case4) passed++;

        System.out.println("Required Test Cases: " + passed + "/" + total + " passed");
    }

    /**
     * Tests specific scenarios assisted by ChatGPT
     * Validates regex patterns and edge cases
     */
    private static void runChatGPTSpecificTests() {
        System.out.println("\n--- CHATGPT SPECIFIC TESTS ---");
        Login login = new Login();
        int passed = 0;
        int total = 0;

        // Test valid South African number formats
        total++;
        boolean test1 = login.checkCellPhoneNumber("+2783123456") &&
                login.checkCellPhoneNumber("+2771123456") &&
                login.checkCellPhoneNumber("+2760123456");
        System.out.println("Test 1 - ChatGPT Regex valid numbers: " + (test1 ? "✓ PASS" : "✗ FAIL"));
        if (test1) passed++;

        // Test invalid number formats
        total++;
        boolean test2 = !login.checkCellPhoneNumber("0831234567") &&
                !login.checkCellPhoneNumber("+44831234567") &&
                !login.checkCellPhoneNumber("2783123456");
        System.out.println("Test 2 - ChatGPT Regex invalid numbers: " + (test2 ? "✓ PASS" : "✗ FAIL"));
        if (test2) passed++;

        System.out.println("ChatGPT Specific Tests: " + passed + "/" + total + " passed");
    }

    /**
     * Tests boundary conditions and edge cases
     * Validates system behavior at limits
     */
    private static void runBoundaryConditionTests() {
        System.out.println("\n--- BOUNDARY CONDITION TESTS ---");
        Login login = new Login();
        Message.resetMessageCount();
        int passed = 0;
        int total = 0;

        // Test exact 250 character boundary
        total++;
        String exact250 = "A".repeat(250);
        Message boundaryMessage = new Message("+271234567", exact250, 1);
        boolean test1 = boundaryMessage.validateMessageLength().equals("Message ready to send.");
        System.out.println("Test 1 - Exact 250 character message: " + (test1 ? "✓ PASS" : "✗ FAIL"));
        if (test1) passed++;

        // Test 251 character boundary (over limit)
        total++;
        String over250 = "A".repeat(251);
        Message overMessage = new Message("+271234567", over250, 2);
        boolean test2 = overMessage.validateMessageLength().equals("Message exceeds 250 characters by 1, please reduce size.");
        System.out.println("Test 2 - 251 character message: " + (test2 ? "✓ PASS" : "✗ FAIL"));
        if (test2) passed++;

        // Test empty message boundary
        total++;
        Message emptyMessage = new Message("+271234567", "", 3);
        boolean test3 = emptyMessage.validateMessageLength().equals("Message ready to send.");
        System.out.println("Test 3 - Empty message validation: " + (test3 ? "✓ PASS" : "✗ FAIL"));
        if (test3) passed++;

        // Test username boundary (5 characters)
        total++;
        boolean test4 = login.checkUserName("ab_cd");
        System.out.println("Test 4 - 5 character username: " + (test4 ? "✓ PASS" : "✗ FAIL"));
        if (test4) passed++;

        // Test phone number boundary (10 characters)
        total++;
        boolean test5 = login.checkCellPhoneNumber("+271234567");
        System.out.println("Test 5 - Valid 10 character phone: " + (test5 ? "✓ PASS" : "✗ FAIL"));
        if (test5) passed++;

        // Comprehensive boundary test for message lengths
        total++;
        Message twoFortyNineChars = new Message("+271234567", "A".repeat(249), 4);
        Message twoFiftyChars = new Message("+271234567", "A".repeat(250), 5);
        Message twoFiftyOneChars = new Message("+271234567", "A".repeat(251), 6);

        boolean test6 = twoFortyNineChars.validateMessageLength().equals("Message ready to send.") &&
                twoFiftyChars.validateMessageLength().equals("Message ready to send.") &&
                twoFiftyOneChars.validateMessageLength().equals("Message exceeds 250 characters by 1, please reduce size.");
        System.out.println("Test 6 - Boundary 249/250/251 chars: " + (test6 ? "✓ PASS" : "✗ FAIL"));
        if (test6) passed++;

        System.out.println("Boundary Condition Tests: " + passed + "/" + total + " passed");
    }
}