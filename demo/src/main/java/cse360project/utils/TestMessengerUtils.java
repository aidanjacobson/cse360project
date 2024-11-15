package cse360project.utils;
import cse360project.InitTestDB;
import cse360project.Message;
import cse360project.User;
import cse360project.utils.MessengerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TestMessengerUtils {

    @BeforeEach
    public void setUp() {
    	InitTestDB.init();
    }
    
    @Test
    public void testGetAllMessagesInUserThread() {
        // Create a test user to simulate a user thread
        User student1 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='student1'");
        User instructor1 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='instructor1'");
        
        // Create the MessageType and Role for the test
        MessageType messageType = MessageType.GENERIC;  // Replace with actual MessageType you are using
        Role studentSenderRole = Role.STUDENT;  // Replace with actual Role (e.g., STUDENT or TEACHER)
        Role instructorSenderRole = Role.INSTRUCTOR;
        
        // Manually add some messages to the thread for this user
        Message message1 = new Message(-1, messageType, "Hi I need help!", student1, studentSenderRole, student1, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message1);
        Message message2 = new Message(-1, messageType, "Hi I can help you!", instructor1, instructorSenderRole, student1, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message2);
        Message message3 = new Message(-1, messageType, "Seperate Thread!", instructor1, instructorSenderRole, instructor1, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message3);
        
        List<Message> messageList = MessengerUtils.getAllMessagesInUserThread(student1);
        // Assertions
        assertNotNull(messageList, "The message list should not be null");
        assertEquals(2, messageList.size(), "The number of messages in the thread should be 2 for student1");

        // Verify the first message in the list is the one from student1
        assertTrue(messageList.get(0).getMessageContent().equals("Hi I need help!"), "The first message should be the one from student1");
        
        // Verify the second message in the list is from the instructor
        assertTrue(messageList.get(1).getMessageContent().equals("Hi I can help you!"), "The second message should be the one from instructor1");
        
        assertFalse(messageList.stream().anyMatch(msg -> msg.getMessageContent().equals("Separate Thread!")), "The separate thread message should not be in student1's thread");
        //Check size of array list is 2
        //Check to make sure that that the message for student1 is the first element of the array list. 
        //Check to make sure message2 is the 2nd element of the array list. 
    }
    
    @Test
    public void testGetAllStudentThreads() {
        // Create test users to represent students
        User student1 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='student1'");
        User student2 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='student2'");
        User instructor1 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='instructor1'");
        User instructor2 = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username='instructor2'");
        
        assertNotNull(student1, "Student1 should not be null");
        assertNotNull(student2, "Student2 should not be null");
        
        // Retrieve all student threads
        MessageType messageType = MessageType.GENERIC;  // Replace with actual MessageType you are using
        Role studentSenderRole = Role.STUDENT;  // Replace with actual Role (e.g., STUDENT or TEACHER)
        Role instructorSenderRole = Role.INSTRUCTOR;
        
        Message message1 = new Message(-1, messageType, "Hello instructor!", student1, studentSenderRole, student1, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message1);
        Message message2 = new Message(-1, messageType, "Hi how can I help you!", instructor1, instructorSenderRole, student1, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message2);
        Message message3 = new Message(-1, messageType, "Help me please!", student2, instructorSenderRole, student2, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message3);
        Message message4 = new Message(-1, messageType, "Seperate Thread!", instructor2, instructorSenderRole, instructor2, new Timestamp(System.currentTimeMillis()));
        DatabaseHelper.addMessage(message4);
        
        List<Message> messageList1 = MessengerUtils.getAllMessagesInUserThread(student1);
        List<Message> messageList2 = MessengerUtils.getAllMessagesInUserThread(student2);
        List<Message> messageList3 = MessengerUtils.getAllMessagesInUserThread(instructor2);
        
        assertEquals(2, messageList1.size(), "Student1's thread should contain 2 messages");
        assertEquals(1, messageList2.size(), "Student2's thread should contain 1 message");
        assertEquals(1, messageList3.size(), "Instructor2's thread should contain 1 message");

     // Test 2: Check that the retrieved thread for student1 includes the correct messages by content
        assertTrue(messageList1.stream().anyMatch(msg -> msg.getMessageContent().equals("Hello instructor!")),
                "Student1's thread should contain the student's message 'Hello instructor!'");
        assertTrue(messageList1.stream().anyMatch(msg -> msg.getMessageContent().equals("Hi, how can I help you?")),
                "Student1's thread should contain the instructor's response 'Hi, how can I help you?'");
        assertTrue(messageList2.stream().anyMatch(msg -> msg.getMessageContent().equals("Help me please!")),
                "Student2's thread should contain the student's message 'Help me please!'");

        assertTrue(messageList3.stream().anyMatch(msg -> msg.getMessageContent().equals("Separate Thread!")),
                "Instructor2's thread should contain the separate message 'Separate Thread!'");

        // Additional checks to verify no incorrect cross-thread messages
        assertFalse(messageList1.stream().anyMatch(msg -> msg.getMessageContent().equals("Help me please!")),
                "Student1's thread should not contain messages from student2");
        assertFalse(messageList2.stream().anyMatch(msg -> msg.getMessageContent().equals("Separate Thread!")),
                "Student2's thread should not contain messages from instructor2");

        // Print diagnostics for debug assistance
        System.out.println("Student1's thread messages: " + messageList1);
        System.out.println("Student2's thread messages: " + messageList2);
        System.out.println("Instructor2's thread messages: " + messageList3);
        //add messages for multiple students. student 1 sends in student 1 thread. etc. student 2 -> 2. instructor 1 replies to student 1 in student 1 thread. Instructor 2 sends a message in instructor 2 thread. 
        //List student 1 thread, 2's thread, instructor 2 thread. 
        //Test 1: size of array list should be 3. 
        //Test 2: return student 1, Test 3: student 2, Test 4: instructor 2. 
    }
}