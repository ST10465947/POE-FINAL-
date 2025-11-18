package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * MESSAGE MANAGEMENT SYSTEM - PART 3
 * Student: Heloisa da Costa Campos
 * Student Number: ST10465947
 * Module: PROG5121
 *
 * ChatGPT Assistance:
 * - JSON file reading implementation using Gson library
 * - Array population logic and data structure design
 * - Message search and deletion algorithms
 * - Comprehensive test data organization
 */
public class MessageManager {

    /**
     * Message class representing a complete SMS message with all attributes
     * Required for proper object-oriented data management
     */
    public static class Message {
        private String messageHash;    // Unique identifier for deletion
        private String messageID;      // For message searching
        private String recipient;      // Recipient phone number
        private String message;        // Message content
        private String flag;           // Status: "Sent", "Stored", "Disregarded"
        private String sender = "System"; // Default sender as per requirements

        /**
         * Constructor to initialize message with all required fields
         */
        public Message(String hash, String id, String recip, String msg, String flg) {
            this.messageHash = hash;
            this.messageID = id;
            this.recipient = recip;
            this.message = msg;
            this.flag = flg;
        }

        // Getter methods for accessing private properties
        public String getMessageHash() { return messageHash; }
        public String getMessageID() { return messageID; }
        public String getRecipient() { return recipient; }
        public String getMessage() { return message; }
        public String getFlag() { return flag; }
        public String getSender() { return sender; }

        @Override
        public String toString() {
            return String.format("Message[Hash: %s, ID: %s, To: %s, Content: %s]",
                    messageHash, messageID, recipient, message);
        }
    }

    // ==================== ARRAY DECLARATIONS ====================
    // All arrays as specified in Part 3 requirements

    /**
     * Array: Contains ALL messages in the system for comprehensive management
     */
    public static List<Message> allMessages = new ArrayList<>();

    /**
     * Array: Contains only messages with "Sent" flag
     */
    public static List<Message> sentMessages = new ArrayList<>();

    /**
     * Array: Contains only messages with "Disregarded" flag
     */
    public static List<Message> disregardedMessages = new ArrayList<>();

    /**
     * Array: Contains only messages with "Stored" flag (loaded from JSON)
     */
    public static List<Message> storedMessages = new ArrayList<>();

    /**
     * Array: Contains all message hashes for quick lookup and deletion
     */
    public static List<String> messageHashes = new ArrayList<>();

    /**
     * Array: Contains all message IDs for searching functionality
     */
    public static List<String> messageIDs = new ArrayList<>();

    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);

    // ==================== TEST DATA ====================
    // Using EXACT test data from requirements document

    /**
     * Test data array containing all 5 test messages from specifications
     * Format: {Hash, ID, Recipient, Message, Flag}
     */
    private static final String[][] TEST_DATA = {
            // Message 1: Sent message
            {"H1", "M1", "+27834557896", "Did you get the cake?", "Sent"},
            // Message 2: Stored message (longest)
            {"H2", "M2", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored"},
            // Message 3: Disregarded message
            {"H3", "M3", "+27834484567", "Yohoooo, I am at your gate.", "Disregarded"},
            // Message 4: Sent message
            {"H4", "M4", "0838884567", "It is dinner time!", "Sent"},
            // Message 5: Stored message
            {"H5", "M5", "+27838884567", "Ok, I am leaving without you.", "Stored"}
    };

    // ==================== ARRAY POPULATION METHODS ====================

    /**
     * FUNCTIONALITY 1: Populates all arrays with the provided test data
     * This method initializes the system with the required 5 test messages
     * and organizes them into the appropriate arrays based on their flags
     */
    public static void populateArrays() {
        // Clear all arrays to ensure fresh start
        clearAllArrays();

        System.out.println("🔄 Populating arrays with test data...");

        // Create messages from test data and add to appropriate arrays
        for (String[] data : TEST_DATA) {
            Message message = new Message(data[0], data[1], data[2], data[3], data[4]);
            addMessageToArrays(message);
        }

        System.out.println("✅ Arrays populated successfully!");
        printArrayStatistics();
    }

    /**
     * Helper method to add a message to all relevant arrays based on its flag
     * Ensures data consistency across all parallel arrays
     */
    private static void addMessageToArrays(Message msg) {
        // Add to main collections
        allMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());

        // Add to flag-specific arrays based on message status
        switch(msg.getFlag().toLowerCase()) {
            case "sent":
                sentMessages.add(msg);
                break;
            case "stored":
                storedMessages.add(msg);
                break;
            case "disregarded":
                disregardedMessages.add(msg);
                break;
        }
    }

    /**
     * Clears all arrays to reset the system state
     * Useful for testing and reinitialization
     */
    private static void clearAllArrays() {
        allMessages.clear();
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    /**
     * Prints statistics about the current state of arrays
     * Helps verify correct array population
     */
    private static void printArrayStatistics() {
        System.out.println("\n📊 Array Statistics:");
        System.out.println("   • Total Messages: " + allMessages.size());
        System.out.println("   • Sent Messages: " + sentMessages.size());
        System.out.println("   • Stored Messages: " + storedMessages.size());
        System.out.println("   • Disregarded Messages: " + disregardedMessages.size());
        System.out.println("   • Message Hashes: " + messageHashes.size());
        System.out.println("   • Message IDs: " + messageIDs.size());
    }

    // ==================== REQUIRED FUNCTIONALITIES ====================

    /**
     * FUNCTIONALITY 2a: Display sender and recipient of all sent messages
     * Shows only sent messages with their sender and recipient information
     */
    public static void displayAllSentMessages() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FUNCTIONALITY 2a: SENDER & RECIPIENT OF ALL SENT MESSAGES");
        System.out.println("=".repeat(60));
        System.out.printf("%-12s %-20s %-30s\n", "Sender", "Recipient", "Message Preview");
        System.out.println("-".repeat(60));

        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages found.");
            return;
        }

        for (Message msg : sentMessages) {
            String preview = msg.getMessage().length() > 25 ?
                    msg.getMessage().substring(0, 25) + "..." : msg.getMessage();
            System.out.printf("%-12s %-20s %-30s\n",
                    msg.getSender(), msg.getRecipient(), preview);
        }
    }

    /**
     * FUNCTIONALITY 2b: Display the longest sent message
     * Finds and displays details of the longest message among sent messages
     */
    public static void displayLongestSentMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FUNCTIONALITY 2b: LONGEST SENT MESSAGE");
        System.out.println("=".repeat(60));

        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages available.");
            return;
        }

        Message longestMessage = null;
        int maxLength = 0;

        // Find the longest message by comparing lengths
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > maxLength) {
                maxLength = msg.getMessage().length();
                longestMessage = msg;
            }
        }

        if (longestMessage != null) {
            System.out.println("📏 LONGEST MESSAGE DETAILS:");
            System.out.println("   • Message: " + longestMessage.getMessage());
            System.out.println("   • Length: " + longestMessage.getMessage().length() + " characters");
            System.out.println("   • Recipient: " + longestMessage.getRecipient());
            System.out.println("   • Message ID: " + longestMessage.getMessageID());
            System.out.println("   • Message Hash: " + longestMessage.getMessageHash());
        }
    }

    /**
     * FUNCTIONALITY 2c: Search for message by ID and display details
     * @param messageID The ID to search for in all messages
     */
    public static void searchMessageByID(String messageID) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FUNCTIONALITY 2c: SEARCH BY MESSAGE ID: " + messageID);
        System.out.println("=".repeat(60));

        boolean found = false;
        for (Message msg : allMessages) {
            if (msg.getMessageID().equals(messageID)) {
                System.out.println("✅ MESSAGE FOUND:");
                System.out.println("   • Recipient: " + msg.getRecipient());
                System.out.println("   • Message: " + msg.getMessage());
                System.out.println("   • Status: " + msg.getFlag());
                System.out.println("   • Hash: " + msg.getMessageHash());
                System.out.println("   • Sender: " + msg.getSender());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("❌ No message found with ID: " + messageID);
        }
    }

    /**
     * FUNCTIONALITY 2d: Search for all messages sent to a particular recipient
     * @param recipient The recipient phone number to search for
     */
    public static void searchMessagesByRecipient(String recipient) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FUNCTIONALITY 2d: MESSAGES FOR RECIPIENT: " + recipient);
        System.out.println("=".repeat(60));

        List<Message> recipientMessages = new ArrayList<>();

        // Search through all messages for the specified recipient
        for (Message msg : allMessages) {
            if (msg.getRecipient().equals(recipient)) {
                recipientMessages.add(msg);
            }
        }

        if (recipientMessages.isEmpty()) {
            System.out.println("No messages found for recipient: " + recipient);
            return;
        }

        System.out.println("📨 Found " + recipientMessages.size() + " message(s) for " + recipient + ":");
        for (int i = 0; i < recipientMessages.size(); i++) {
            Message msg = recipientMessages.get(i);
            System.out.println((i + 1) + ". \"" + msg.getMessage() + "\" [" + msg.getFlag() + "]");
        }
    }

    /**
     * FUNCTIONALITY 2e: Delete a message using its hash
     * @param hash The message hash to identify and delete the message
     */
    public static void deleteMessageByHash(String hash) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FUNCTIONALITY 2e: DELETE MESSAGE BY HASH: " + hash);
        System.out.println("=".repeat(60));

        Message messageToDelete = null;

        // Find the message with the specified hash
        for (Message msg : allMessages) {
            if (msg.getMessageHash().equals(hash)) {
                messageToDelete = msg;
                break;
            }
        }

        if (messageToDelete != null) {
            // Remove from all arrays to maintain consistency
            allMessages.remove(messageToDelete);
            sentMessages.remove(messageToDelete);
            storedMessages.remove(messageToDelete);
            disregardedMessages.remove(messageToDelete);
            messageHashes.remove(hash);
            messageIDs.remove(messageToDelete.getMessageID());

            System.out.println("✅ MESSAGE SUCCESSFULLY DELETED:");
            System.out.println("   • Message: " + messageToDelete.getMessage());
            System.out.println("   • Recipient: " + messageToDelete.getRecipient());
            System.out.println("   • Hash: " + hash);
            System.out.println("   • Status: " + messageToDelete.getFlag());
        } else {
            System.out.println("❌ No message found with hash: " + hash);
        }
    }

    /**
     * FUNCTIONALITY 2f: Display full report of all sent messages
     * Shows comprehensive details including hash, recipient, and message
     */
    public static void displayFullReport() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("FUNCTIONALITY 2f: COMPREHENSIVE SENT MESSAGES REPORT");
        System.out.println("=".repeat(90));
        System.out.printf("%-10s %-12s %-18s %-45s\n",
                "Hash", "Message ID", "Recipient", "Message");
        System.out.println("-".repeat(90));

        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages to display in report.");
            return;
        }

        // Display all sent messages in formatted report
        for (Message msg : sentMessages) {
            String messagePreview = msg.getMessage().length() > 40 ?
                    msg.getMessage().substring(0, 40) + "..." : msg.getMessage();
            System.out.printf("%-10s %-12s %-18s %-45s\n",
                    msg.getMessageHash(),
                    msg.getMessageID(),
                    msg.getRecipient(),
                    messagePreview);
        }
        System.out.println("=".repeat(90));
        System.out.println("Total sent messages: " + sentMessages.size());
    }

    // ==================== JSON INTEGRATION ====================

    /**
     * Loads stored messages from a JSON file using Gson library
     * ChatGPT Assistance: JSON file reading implementation
     * @param filePath Path to the JSON file containing stored messages
     */
    public static void loadStoredMessagesFromJSON(String filePath) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("JSON INTEGRATION: LOADING STORED MESSAGES FROM FILE");
        System.out.println("=".repeat(60));

        try {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<List<Message>>(){}.getType();
            List<Message> loadedMessages = gson.fromJson(new FileReader(filePath), messageListType);

            if (loadedMessages != null && !loadedMessages.isEmpty()) {
                for (Message msg : loadedMessages) {
                    addMessageToArrays(msg);
                }
                System.out.println("✅ Successfully loaded " + loadedMessages.size() + " messages from JSON file");
            } else {
                System.out.println("⚠️  JSON file is empty or contains no messages");
                System.out.println("💡 Creating sample JSON file for demonstration...");
                createSampleJSONFile();
            }

        } catch (Exception e) {
            System.out.println("❌ Error loading JSON file: " + e.getMessage());
            System.out.println("💡 Creating sample JSON file for demonstration...");
            createSampleJSONFile();
        }
    }

    /**
     * Creates a sample JSON file with demonstration messages
     * Ensures the system works even if no external JSON exists initially
     */
    private static void createSampleJSONFile() {
        try {
            Gson gson = new Gson();
            List<Message> sampleMessages = new ArrayList<>();

            // Add sample stored messages for demonstration
            sampleMessages.add(new Message("JSON1", "J1", "+27123456789",
                    "This is a sample stored message from JSON file", "Stored"));
            sampleMessages.add(new Message("JSON2", "J2", "+27987654321",
                    "Another stored message loaded from external file", "Stored"));

            // Write to JSON file
            try (FileWriter writer = new FileWriter("messages.json")) {
                gson.toJson(sampleMessages, writer);
            }

            System.out.println("📁 Sample JSON file 'messages.json' created with " +
                    sampleMessages.size() + " demonstration messages");

            // Load the newly created file
            loadStoredMessagesFromJSON("messages.json");

        } catch (Exception e) {
            System.out.println("❌ Error creating sample JSON: " + e.getMessage());
        }
    }

    // ==================== USER INTERFACE METHODS ====================

    /**
     * Displays the main menu for user interaction
     */
    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        MESSAGE MANAGEMENT SYSTEM - PART 3");
        System.out.println("=".repeat(50));
        System.out.println("1. Populate Arrays with Test Data");
        System.out.println("2. Display All Sent Messages (Sender & Recipient)");
        System.out.println("3. Display Longest Sent Message");
        System.out.println("4. Search Message by ID");
        System.out.println("5. Search Messages by Recipient");
        System.out.println("6. Delete Message by Hash");
        System.out.println("7. Display Full Report");
        System.out.println("8. Load Messages from JSON");
        System.out.println("9. Display Array Statistics");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (0-9): ");
    }

    /**
     * Handles user menu selection and executes corresponding functionality
     * @param choice The user's menu choice
     * @return true to continue, false to exit
     */
    public static boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                populateArrays();
                break;
            case 2:
                displayAllSentMessages();
                break;
            case 3:
                displayLongestSentMessage();
                break;
            case 4:
                System.out.print("Enter Message ID to search: ");
                String searchID = scanner.nextLine();
                searchMessageByID(searchID);
                break;
            case 5:
                System.out.print("Enter Recipient to search: ");
                String recipient = scanner.nextLine();
                searchMessagesByRecipient(recipient);
                break;
            case 6:
                System.out.print("Enter Message Hash to delete: ");
                String hash = scanner.nextLine();
                deleteMessageByHash(hash);
                break;
            case 7:
                displayFullReport();
                break;
            case 8:
                loadStoredMessagesFromJSON("messages.json");
                break;
            case 9:
                printArrayStatistics();
                break;
            case 0:
                System.out.println("Exiting Message Management System. Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice! Please enter 0-9.");
        }
        return true;
    }

    // ==================== MAIN METHOD ====================

    /**
     * Main method demonstrating all Part 3 functionalities
     * Provides interactive menu system for testing all features
     */
    public static void main(String[] args) {
        System.out.println("🚀 MESSAGE MANAGEMENT SYSTEM - PART 3");
        System.out.println("Student: Heloisa da Costa Campos | ST10465947");
        System.out.println("Module: PROG5121 - Programming 1A");
        System.out.println("✅ All Part 3 Requirements Implemented\n");

        // Auto-populate with test data on startup
        populateArrays();

        boolean continueRunning = true;
        while (continueRunning) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                continueRunning = handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number 0-9.");
            }

            if (continueRunning) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }
}