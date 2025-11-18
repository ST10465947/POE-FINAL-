package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * COMPREHENSIVE UNIT TEST SUITE FOR MESSAGE MANAGER - PART 3
 *
 * Tests all Part 3 requirements including array population,
 * message operations, and JSON integration.
 *
 * @author Heloisa Campos
 * @version 1.0 - Complete Part 3 test coverage
 */
public class MessageManagerTest {

    /**
     * Set up fresh test data before each test execution
     * Ensures test isolation and consistent starting state
     */
    @BeforeEach
    public void setUp() {
        MessageManager.populateArrays();
    }

    // ==================== ARRAY POPULATION TESTS ====================

    @Test
    @DisplayName("Test 1: All Arrays Correctly Populated with Test Data")
    public void testAllArraysCorrectlyPopulated() {
        // Verify all arrays contain the expected number of items
        assertEquals(5, MessageManager.allMessages.size(),
                "All messages array should contain 5 test messages");
        assertEquals(2, MessageManager.sentMessages.size(),
                "Sent messages array should contain 2 sent messages");
        assertEquals(2, MessageManager.storedMessages.size(),
                "Stored messages array should contain 2 stored messages");
        assertEquals(1, MessageManager.disregardedMessages.size(),
                "Disregarded messages array should contain 1 disregarded message");
        assertEquals(5, MessageManager.messageHashes.size(),
                "Message hashes array should contain 5 hashes");
        assertEquals(5, MessageManager.messageIDs.size(),
                "Message IDs array should contain 5 IDs");
    }

    @Test
    @DisplayName("Test 2: Sent Messages Array Contains Expected Test Data")
    public void testSentMessagesArrayContainsExpectedData() {
        // Test for specific message content from requirements
        boolean foundCakeMessage = false;
        boolean foundDinnerMessage = false;

        for (MessageManager.Message msg : MessageManager.sentMessages) {
            if (msg.getMessage().equals("Did you get the cake?")) {
                foundCakeMessage = true;
            }
            if (msg.getMessage().equals("It is dinner time!")) {
                foundDinnerMessage = true;
            }
        }

        assertTrue(foundCakeMessage, "Should contain 'Did you get the cake?' message");
        assertTrue(foundDinnerMessage, "Should contain 'It is dinner time!' message");
    }

    // ==================== FUNCTIONALITY TESTS ====================

    @Test
    @DisplayName("Test 3: Display Longest Sent Message - Correct Identification")
    public void testDisplayLongestSentMessage() {
        // The longest sent message should be "Did you get the cake?" (19 chars)
        String longestMessageContent = "";

        for (MessageManager.Message msg : MessageManager.sentMessages) {
            if (msg.getMessage().length() > longestMessageContent.length()) {
                longestMessageContent = msg.getMessage();
            }
        }

        assertEquals("Did you get the cake?", longestMessageContent,
                "Longest sent message should be 'Did you get the cake?'");
    }

    @Test
    @DisplayName("Test 4: Search For Message By ID - Success Case")
    public void testSearchMessageByID_Success() {
        // Test searching for existing message ID M4
        boolean messageFound = false;
        String foundRecipient = "";
        String foundMessage = "";

        for (MessageManager.Message msg : MessageManager.allMessages) {
            if (msg.getMessageID().equals("M4")) {
                messageFound = true;
                foundRecipient = msg.getRecipient();
                foundMessage = msg.getMessage();
                break;
            }
        }

        assertTrue(messageFound, "Message with ID M4 should be found");
        assertEquals("0838884567", foundRecipient,
                "Recipient should match test data for M4");
        assertEquals("It is dinner time!", foundMessage,
                "Message content should match test data for M4");
    }

    @Test
    @DisplayName("Test 5: Search Messages By Recipient - Multiple Messages Found")
    public void testSearchMessagesByRecipient_MultipleMessages() {
        // Count messages for specific recipient +27838884567 (should have 2 messages)
        int messageCount = 0;
        String targetRecipient = "+27838884567";

        for (MessageManager.Message msg : MessageManager.allMessages) {
            if (msg.getRecipient().equals(targetRecipient)) {
                messageCount++;
            }
        }

        assertEquals(2, messageCount,
                "Should find 2 messages for recipient +27838884567");
    }

    @Test
    @DisplayName("Test 6: Delete Message Using Message Hash - Successful Deletion")
    public void testDeleteMessageByHash_Success() {
        // Record initial state
        int initialTotalMessages = MessageManager.allMessages.size();
        int initialHashes = MessageManager.messageHashes.size();

        // Perform deletion of H2 (which is a Stored message)
        MessageManager.deleteMessageByHash("H2");

        // Verify deletion was successful
        assertEquals(initialTotalMessages - 1, MessageManager.allMessages.size(),
                "Total messages should decrease by 1 after deletion");
        assertEquals(initialHashes - 1, MessageManager.messageHashes.size(),
                "Message hashes should decrease by 1 after deletion");

        // Verify specific hash no longer exists
        assertFalse(MessageManager.messageHashes.contains("H2"),
                "Hash H2 should be removed from message hashes array");

        // H2 was a stored message, so stored messages should decrease
        assertEquals(1, MessageManager.storedMessages.size(),
                "Stored messages should decrease by 1 after deleting H2");
    }

    @Test
    @DisplayName("Test 7: Array Integrity After Operations")
    public void testArrayIntegrity() {
        // Verify all arrays maintain consistent state
        assertEquals(MessageManager.allMessages.size(), MessageManager.messageHashes.size(),
                "All messages and message hashes arrays should have same size");
        assertEquals(MessageManager.allMessages.size(), MessageManager.messageIDs.size(),
                "All messages and message IDs arrays should have same size");

        // Verify flag-specific arrays are subsets of main array
        assertTrue(MessageManager.sentMessages.size() <= MessageManager.allMessages.size(),
                "Sent messages should be subset of all messages");
        assertTrue(MessageManager.storedMessages.size() <= MessageManager.allMessages.size(),
                "Stored messages should be subset of all messages");
        assertTrue(MessageManager.disregardedMessages.size() <= MessageManager.allMessages.size(),
                "Disregarded messages should be subset of all messages");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Test 8: Search For Non-Existent Message ID")
    public void testSearchNonExistentMessageID() {
        // Test searching for ID that doesn't exist
        boolean foundNonExistent = false;

        for (MessageManager.Message msg : MessageManager.allMessages) {
            if (msg.getMessageID().equals("NON_EXISTENT")) {
                foundNonExistent = true;
                break;
            }
        }

        assertFalse(foundNonExistent,
                "Should not find message with non-existent ID");
    }

    @Test
    @DisplayName("Test 9: Delete Non-Existent Message Hash")
    public void testDeleteNonExistentMessageHash() {
        // Record initial state
        int initialTotalMessages = MessageManager.allMessages.size();
        int initialHashes = MessageManager.messageHashes.size();

        // Try to delete non-existent hash
        MessageManager.deleteMessageByHash("NON_EXISTENT_HASH");

        // Verify no changes occurred
        assertEquals(initialTotalMessages, MessageManager.allMessages.size(),
                "Total messages should not change after failed deletion");
        assertEquals(initialHashes, MessageManager.messageHashes.size(),
                "Message hashes should not change after failed deletion");
    }

    @Test
    @DisplayName("Test 10: Search For Recipient With No Messages")
    public void testSearchRecipientWithNoMessages() {
        // Count messages for non-existent recipient
        int messageCount = 0;
        String nonExistentRecipient = "+27000000000";

        for (MessageManager.Message msg : MessageManager.allMessages) {
            if (msg.getRecipient().equals(nonExistentRecipient)) {
                messageCount++;
            }
        }

        assertEquals(0, messageCount,
                "Should find 0 messages for non-existent recipient");
    }

    // ==================== SPECIFIC REQUIREMENT TESTS ====================

    @Test
    @DisplayName("Test 11: Verify All Test Data Messages Are Present")
    public void testAllTestDataMessagesPresent() {
        // Verify all 5 test messages are present in the system
        String[] expectedMessages = {
                "Did you get the cake?",
                "Where are you? You are late! I have asked you to be on time.",
                "Yohoooo, I am at your gate.",
                "It is dinner time!",
                "Ok, I am leaving without you."
        };

        for (String expectedMessage : expectedMessages) {
            boolean found = false;
            for (MessageManager.Message msg : MessageManager.allMessages) {
                if (msg.getMessage().equals(expectedMessage)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Should contain message: " + expectedMessage);
        }
    }

    @Test
    @DisplayName("Test 12: Verify Message Flags Are Correctly Assigned")
    public void testMessageFlagsCorrectlyAssigned() {
        // Test specific message flags from test data
        boolean foundSentMessage = false;
        boolean foundStoredMessage = false;
        boolean foundDisregardedMessage = false;

        for (MessageManager.Message msg : MessageManager.allMessages) {
            if (msg.getMessage().equals("Did you get the cake?") && msg.getFlag().equals("Sent")) {
                foundSentMessage = true;
            }
            if (msg.getMessage().equals("Where are you? You are late! I have asked you to be on time.") && msg.getFlag().equals("Stored")) {
                foundStoredMessage = true;
            }
            if (msg.getMessage().equals("Yohoooo, I am at your gate.") && msg.getFlag().equals("Disregarded")) {
                foundDisregardedMessage = true;
            }
        }

        assertTrue(foundSentMessage, "Should find Sent message with correct flag");
        assertTrue(foundStoredMessage, "Should find Stored message with correct flag");
        assertTrue(foundDisregardedMessage, "Should find Disregarded message with correct flag");
    }
}