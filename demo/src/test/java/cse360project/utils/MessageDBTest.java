package cse360project.utils;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import cse360project.Message;
import cse360project.User;
import cse360project.utils.MessageType;
import cse360project.utils.Role;

public class MessageDBTest {

    @Before
    public void setup() throws SQLException {
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllMessages();
        DatabaseHelper.deleteAllUsers();
        DatabaseHelper.createMessagesTable();
    }

    @Test
    public void testMessageDBEmpty() {
        assertTrue("Running test to check if message database is empty", DatabaseHelper.isMessagesDatabaseEmpty());
    }

    @Test
    public void testAddMessage() {
        // Setup a sample user as the sender and thread for the message
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        User threadUser = new User(-1, "threadUser", "password".toCharArray(), "thread@example.com", null, true, false, null, "Jane", null, "Doe", "Janie", false, false, true, new ArrayList<>());

        // Add sample users to the database
        DatabaseHelper.addUser(sender);
        DatabaseHelper.addUser(threadUser);

        // Create and add a message
        Message message = new Message(MessageType.SPECIFIC, "Test message content", sender, Role.STUDENT, threadUser, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message);

        // Check if the message database is no longer empty
        assertFalse("Message database should not be empty after adding a message", DatabaseHelper.isMessagesDatabaseEmpty());
    }

    @Test
    public void testGetSpecificMessages() {
        // Setup a sample message with SPECIFIC type
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        User threadUser = new User(-1, "threadUser", "password".toCharArray(), "thread@example.com", null, true, false, null, "Jane", null, "Doe", "Janie", false, false, true, new ArrayList<>());
        
        DatabaseHelper.addUser(sender);
        DatabaseHelper.addUser(threadUser);
        
        Message message1 = new Message(MessageType.SPECIFIC, "Specific message 1", sender, Role.STUDENT, threadUser, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(MessageType.GENERIC, "Generic message", sender, Role.STUDENT, threadUser, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message1);
        DatabaseHelper.addMessage(message2);

        // Retrieve specific messages
        ArrayList<Message> specificMessages = SpecificMessageConsolidator.getAllSpecificMessages();

        // Assertions
        assertEquals(1, specificMessages.size());
        assertEquals("Specific message 1", specificMessages.get(0).getMessageContent());
    }
    
    @Test
    public void testGetOneMessage() {
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        DatabaseHelper.addUser(sender);

        Message message = new Message(MessageType.GENERIC, "Single message content", sender, Role.STUDENT, null, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message);

        // Retrieve message using its content to identify it
        Message retrievedMessage = DatabaseHelper.getOneMessage("SELECT * FROM cse360messages WHERE messageContent = 'Single message content'");
        assertNotNull("Message should be retrieved from the database", retrievedMessage);
        assertEquals("Message content should match", "Single message content", retrievedMessage.getMessageContent());
    }

    @Test
    public void testGetAllMessages() {
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        DatabaseHelper.addUser(sender);

        // Add multiple messages
        Message message1 = new Message(MessageType.GENERIC, "Message 1", sender, Role.STUDENT, null, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(MessageType.SPECIFIC, "Message 2", sender, Role.STUDENT, null, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message1);
        DatabaseHelper.addMessage(message2);

        // Retrieve all messages
        ArrayList<Message> allMessages = DatabaseHelper.getAllMessages();
        assertEquals("There should be two messages in the database", 2, allMessages.size());
    }

    @Test
    public void testUpdateMessage() {
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        DatabaseHelper.addUser(sender);

        // Add a message
        Message message = new Message(MessageType.GENERIC, "Original content", sender, Role.STUDENT, null, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message);

        // Retrieve and update the message
        Message retrievedMessage = DatabaseHelper.getOneMessage("SELECT * FROM cse360messages WHERE messageContent = 'Original content'");
        assertNotNull("Message should be retrieved from the database", retrievedMessage);

        // Update message content
        retrievedMessage.setMessageContent("Updated content");
        DatabaseHelper.updateMessage(retrievedMessage);

        // Retrieve the updated message and check content
        Message updatedMessage = DatabaseHelper.getOneMessage("SELECT * FROM cse360messages WHERE messageContent = 'Updated content'");
        assertNotNull("Updated message should be retrieved from the database", updatedMessage);
        assertEquals("Message content should be updated", "Updated content", updatedMessage.getMessageContent());
    }


    @Test
    public void testDeleteMessage() {
        // Setup sample message
        User sender = new User(-1, "senderUser", "password".toCharArray(), "sender@example.com", null, true, false, null, "John", null, "Doe", "Johnny", false, false, true, new ArrayList<>());
        User threadUser = new User(-1, "threadUser", "password".toCharArray(), "thread@example.com", null, true, false, null, "Jane", null, "Doe", "Janie", false, false, true, new ArrayList<>());
        
        DatabaseHelper.addUser(sender);
        DatabaseHelper.addUser(threadUser);
        
        Message message = new Message(MessageType.SPECIFIC, "Test delete message", sender, Role.STUDENT, threadUser, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message);

        // Delete the message
        assertTrue("Message should be deleted successfully", DatabaseHelper.deleteMessage(message));
    } 
}
