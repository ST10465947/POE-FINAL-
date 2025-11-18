package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Message class functionality
 * Tests message creation, validation, hashing, and processing
 * Validates exact specifications from requirements document
 *
 * @author Heloisa Campos
 * @version 1.0 - Complete message testing suite
 */
public class MessageTest {
    private Message message;

    /**
     * Resets message count before each test
     * Ensures clean state and isolated test execution
     */
    @BeforeEach
    public void setUp() {
        Message.resetMessageCount();
    }

    /**
     * Tests exact test data from specifications - Message 1
     * Validates all aspects of a correctly formatted message
     */
    @Test
    public void testMessage1_ExactTestDataFromSpecs() {
        message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);

        // Validate all message properties
        assertTrue(message.checkMessageID());
        assertEquals(1, message.checkRecipientCell());
        assertEquals("Message ready to send.", message.validateMessageLength());
        assertEquals("Cell phone number successfully captured.", message.validateRecipient());

        // Validate exact hash format from specifications
        String hash = message.createMessageHash();
        assertTrue(hash.matches("\\d{2}:0:HITONIGHT"),
                "Hash should be in format XX:0:HITONIGHT but was: " + hash);
    }

    /**
     * Tests exact test data from specifications - Message 2
     * Validates error handling for invalid recipient format
     */
    @Test
    public void testMessage2_ExactTestDataFromSpecs() {
        message = new Message("08575975889", "Hi Keegan, did you receive the payment?", 2);

        assertEquals(0, message.checkRecipientCell());
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.validateRecipient());
    }

    /**
     * Tests message hash generation with exact format requirements
     * Validates three-part structure with correct separators
     */
    @Test
    public void testMessageHash_ExactFormatRequired() {
        message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);
        String hash = message.createMessageHash();

        // Parse and validate hash components
        String[] parts = hash.split(":");
        assertEquals(3, parts.length);
        assertEquals(2, parts[0].length());
        assertTrue(parts[0].matches("\\d{2}"));
        assertEquals("0", parts[1]);
        assertEquals("HITONIGHT", parts[2]);
    }

    /**
     * Tests message length validation with 251 characters
     * Should fail with exact error message from specifications
     */
    @Test
    public void testMessageLength_Exceeds250Characters() {
        String longMessage = "A".repeat(251);
        message = new Message("+27831234567", longMessage, 1);

        String validation = message.validateMessageLength();
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", validation);
    }

    /**
     * Tests message length validation with exactly 250 characters
     * Should pass validation as boundary case
     */
    @Test
    public void testMessageLength_Exactly250Characters() {
        String exact250 = "A".repeat(250);
        message = new Message("+27831234567", exact250, 1);

        assertEquals("Message ready to send.", message.validateMessageLength());
    }

    /**
     * Tests message length validation with 249 characters
     * Should pass validation as under limit
     */
    @Test
    public void testMessageLength_249Characters() {
        String shortMessage = "A".repeat(249);
        message = new Message("+27831234567", shortMessage, 1);

        assertEquals("Message ready to send.", message.validateMessageLength());
    }

    /**
     * Tests message sending action logic
     * Validates all possible return values from sentMessage method
     */
    @Test
    public void testSentMessage_ActionLogic() {
        message = new Message("+27718693002", "Test message", 1);

        String result = message.sentMessage();
        assertNotNull(result);
        // Validate all possible action results
        assertTrue(result.equals("Message successfully sent.") ||
                result.equals("Press 0 to delete message.") ||
                result.equals("Message successfully stored.") ||
                result.equals("Message action cancelled."));
    }

    /**
     * Tests cell phone validation for correct and incorrect formats
     * Validates recipient number parsing and validation
     */
    @Test
    public void testCellPhoneValidation_CorrectFormat() {
        Message validMessage = new Message("+271234567", "Test", 1);
        Message invalidMessage = new Message("0831234567", "Test", 2);

        assertEquals(1, validMessage.checkRecipientCell());
        assertEquals(0, invalidMessage.checkRecipientCell());
    }

    /**
     * Tests special case hash generation for HITONIGHT
     * Validates exact specification requirement for test data
     */
    @Test
    public void testMessageHash_SpecialCaseHITONIGHT() {
        message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);
        String hash = message.createMessageHash();

        assertTrue(hash.matches("\\d{2}:0:HITONIGHT"),
                "Special message should generate HITONIGHT hash, but got: " + hash);
    }

    /**
     * Tests recipient validation success message
     * Validates exact string format from specifications
     */
    @Test
    public void testValidateRecipient_SuccessMessage() {
        Message validMessage = new Message("+27831234567", "Test", 1);
        assertEquals("Cell phone number successfully captured.", validMessage.validateRecipient());
    }

    /**
     * Tests recipient validation failure message
     * Validates exact string format from specifications
     */
    @Test
    public void testValidateRecipient_FailureMessage() {
        Message invalidMessage = new Message("0831234567", "Test", 1);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                invalidMessage.validateRecipient());
    }
}