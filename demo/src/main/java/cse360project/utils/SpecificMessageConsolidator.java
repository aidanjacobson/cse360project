package cse360project.utils;

import cse360project.Message;
import java.util.ArrayList;

public class SpecificMessageConsolidator {

    /**
     * Retrieves all messages from the database with the message type "SPECIFIC".
     * @return an ArrayList<Message> containing all messages with MessageType.SPECIFIC.
     */
    public static ArrayList<Message> getAllSpecificMessages() {
        // Get all messages from the database
        ArrayList<Message> allMessages = DatabaseHelper.getAllMessages();
        
        // Filter messages to only include those with the SPECIFIC type
        ArrayList<Message> specificMessages = new ArrayList<>();
        for (Message message : allMessages) {
            if (message.getMessageType() == MessageType.SPECIFIC) {
                specificMessages.add(message);
            }
        }
        
        return specificMessages;
    }
}
