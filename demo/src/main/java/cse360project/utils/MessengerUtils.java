package cse360project.utils;


import java.util.ArrayList;
import cse360project.Message;
import cse360project.User;

public class MessengerUtils {

    /**
     * Sends a message by adding it to a database.
     * @param message The message to send.
     * @return True if the message was sent successfully, false otherwise.
     */
	public static boolean sendMessage(Message message) {
        try {
            // Assume we add the message to a database or a data structure here
            DatabaseHelper.addMessage(message);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all messages in a specific user's thread.
     * @param threadUser The user whose thread messages to retrieve.
     * @return A list of messages in the specified user's thread.
     */
	public static ArrayList<Message> getAllMessagesInUserThread(User threadUser) {
	    try {
	        // Fetch all messages from the database
	        ArrayList<Message> allMessages = DatabaseHelper.getAllMessages();  // Fetch all messages
	        ArrayList<Message> userMessages = new ArrayList<>();

	        // Filter messages by user ID (both sender and receiver)
	        for (Message msg : allMessages) {
	            // Check if the thread or sender matches the threadUser's ID
	            if (msg.getSender().id == threadUser.id || (msg.getThread() != null && msg.getThread().id == threadUser.id)) {
	                userMessages.add(msg);
	            }
	        }
	        return userMessages;

	    } catch (Exception e) {
	        System.err.println("Error retrieving messages in thread for user: " + threadUser.username);
	        e.printStackTrace();
	        return new ArrayList<>(); // Return an empty list if there was an error
	    }
    }

    /**
     * Retrieves all student threads.
     * @return A list of all student threads.
     */
    public static ArrayList<User> getAllStudentThreads() {
        try {
            // Fetch all messages from the database
            ArrayList<Message> allMessages = DatabaseHelper.getAllMessages();  // Fetch all messages
            ArrayList<User> studentThreads = new ArrayList<>();

            // We can filter messages to find users who have sent or received messages
            for (Message msg : allMessages) {
                User user = msg.getSender(); // Assuming Message has a method to get the sender
                if (user != null && user.is_student && !studentThreads.contains(user)) {
                    studentThreads.add(user);
                }
            }
            return studentThreads;

        } catch (Exception e) {
            System.err.println("Error retrieving student threads: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }
}